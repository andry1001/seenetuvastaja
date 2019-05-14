#############################################################################################
# kood pärineb selleltl lehelt:                                                             #
# https://stackoverflow.com/questions/53256877/how-to-convert-kerash5-file-to-a-tflite-file #
#############################################################################################

import tensorflow as tf
converter = tf.lite.TFLiteConverter.from_keras_model_file('.\model.h5' )
tflite_model = converter.convert()
open( 'model.tflite' , 'wb' ).write( tflite_model )
