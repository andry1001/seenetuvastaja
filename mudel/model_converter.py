import tensorflow as tf
converter = tf.lite.TFLiteConverter.from_keras_model_file('.\model.h5' )
tflite_model = converter.convert()
open( 'model.tflite' , 'wb' ).write( tflite_model )