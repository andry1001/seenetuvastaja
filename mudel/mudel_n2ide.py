#################################
# SEE ON MUDELI LOOMISE NÄIDE   #
#################################
import cv2
import os
import glob
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Model
from keras.layers import Input, Dropout, Dense, Conv2D, Flatten, Activation, MaxPooling2D, BatchNormalization
from keras.optimizers import Adam
from keras.optimizers import SGD
from keras.models import load_model

img_size = 96

#LOEN KÕIK PILDID SISSE

#igale labelile vastab kindel number
labels = []
#iga sample datasetis on kujul [image, class_nr]
dataset = []
BASEDIR = ".\ANDMED"

for SUBDIR in os.listdir(BASEDIR):
    path = os.path.join(BASEDIR, SUBDIR)
    extracted = os.path.basename(os.path.normpath(path))
    label = extracted.lower()
    labels.append(label)
    for file in os.listdir(path):
        img_path = os.path.join(path, file)
        img = cv2.imread(img_path)
        img = cv2.resize(img, (int(img_size), int(img_size)))
        dataset.append([img, labels.index(label)])

#Jagan adnmed train/validation/test setideks
#Näide saadud järgnevalt lehelt:
#https://scikit-learn.org/0.18/modules/generated/sklearn.model_selection.train_test_split.html

dataset = np.array(dataset)
np.random.shuffle(dataset)
X_data, y_data = dataset[:, 0], dataset[:, 1]

X_train, X_test, y_train, y_test = train_test_split(X_data, y_data, test_size=0.3, random_state=42)
X_test, X_val, y_test, y_val = train_test_split(X_test, y_test, test_size=0.5, random_state=42)

X_train = np.stack(X_train)
X_test = np.stack(X_test)
X_val = np.stack(X_val)

print('X_train shape: ' + str(X_train.shape))
print('X_test shape: ' + str(X_test.shape))
print('X_val shape: ' + str(X_val.shape))
print('Klasse kokku: ' + str(len(labels)))

epochs = 100 
batch_size = 32

#Andmete suurendamine
#Näide võetud järgnevalt leheküljelt:
#https://blog.keras.io/building-powerful-image-classification-models-using-very-little-data.html

train_data_gen = ImageDataGenerator(
        featurewise_center=True,
        featurewise_std_normalization=True,
        rotation_range=45,
        width_shift_range=0.4,
        height_shift_range=0.4,
        shear_range=0.3,
        zoom_range=0.3,
        horizontal_flip=True,
        fill_mode='nearest')
train_data_gen.fit(X_train)

test_data_gen = ImageDataGenerator(
        featurewise_center=True,
        featurewise_std_normalization=True)
test_data_gen.fit(X_train)

val_data_gen = ImageDataGenerator(
        featurewise_center=True,
        featurewise_std_normalization=True)
val_data_gen.fit(X_train)

train_gen = train_data_gen.flow(
        X_train,
        y_train,
        batch_size=batch_size)

test_gen = test_data_gen.flow(
        X_test,
        y_test,
        batch_size=batch_size)

val_gen = val_data_gen.flow(
        X_val,
        y_val,
        batch_size=batch_size)
		
#Mudelo loomine

x = Input(shape=(192, 192, 3))

h = Conv2D(32, (3, 3))(x)
h = Activation('relu')(h)
h = MaxPooling2D(pool_size=(2, 2))(h)

h = Flatten()(h)
h = Dense(64)(h)
h = Activation('relu')(h)
h = Dense(len(labels))(h)
p = Activation('softmax')(h)
 
model = Model(inputs=x, outputs=p)
model.compile(loss='sparse_categorical_crossentropy', 
              optimizer=SGD(lr=0.01), 
              metrics=['accuracy'])

history = model.fit_generator(
        train_gen,
        steps_per_epoch=len(X_train) // batch_size,
        epochs=epochs,
        validation_data=val_gen,
        validation_steps=(len(X_val) // batch_size))

model.save('model.h5')

model = load_model('./model.h5')
model.summary()

#Täpsuste arvutamine

model.evaluate_generator(train_gen)
model.evaluate_generator(test_gen)
model.evaluate_generator(val_gen)


