package com.seenetuvastaja.seenetuvastaja.model;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Classifier {

    private static int IMG_SIZE = 96;

    //Treeninghulga keskmised ja standardhälved pikslite kaupa.
    // Võetud kujul [[[B, G, R]]] närvivõrgu mudelist.
    private static float MEAN_B = 61.236965f;
    private static float MEAN_G = 83.059135f;
    private static float MEAN_R = 85.63649f;
    private static float STD_B = 54.313713f;
    private static float STD_G = 55.401028f;
    private static float STD_R = 59.339054f;

    /*
    Järgneva meetodi loomisel on võetud aluseks kood leheküljelt:
    https://www.tensorflow.org/lite/models/image_classification/android
     */
    private static MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /*
    Järgneva meetodi loomisel on võetud aluseks kood leheküljelt:
    https://www.tensorflow.org/lite/models/image_classification/android
     */
    public static float[][] classify(Bitmap image, Activity activity) throws IOException {
        //TODO Kasutab aegunud koodi, mida tulevates versioonides enam võimalik kasutada ei ole!
        //TODO Leia muu lahendus siia tulevikus!
        Interpreter tflite = new Interpreter(loadModelFile(activity));
        Bitmap resizedImage = Bitmap.createScaledBitmap(image, IMG_SIZE, IMG_SIZE, true);
        ByteBuffer imgData = ByteBuffer.allocateDirect(4 * IMG_SIZE * IMG_SIZE * 3);
        imgData.order(ByteOrder.nativeOrder());
        int[] intValues = new int[IMG_SIZE * IMG_SIZE];
        resizedImage.getPixels(intValues, 0, resizedImage.getWidth(), 0, 0, resizedImage.getWidth(), resizedImage.getHeight());
        int pixel = 0;
        for (int i = 0; i < IMG_SIZE; ++i) {
            for (int j = 0; j < IMG_SIZE; ++j) {
                final int val = intValues[pixel++];
                imgData.putFloat((((val) & 0xFF)-MEAN_B)/STD_B);
                imgData.putFloat((((val >> 8) & 0xFF)-MEAN_G)/STD_G);
                imgData.putFloat((((val >> 16) & 0xFF)-MEAN_R)/STD_R);
            }
        }
        float[][] output = new float[1][43];
        tflite.run(imgData, output);
        return output;
    }

    public static float[] getTopNProbabilities(final float[][] output, int n) {
        if (n > output[0].length) return null;
        Float[] probabilities = new Float[output[0].length];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = output[0][i];
        }
        Arrays.sort(probabilities, Collections.reverseOrder());
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = probabilities[i];
        }
        return result;
    }

    public static int[] getTopNIndexes(final float[][] output, int n) {
        if (n > output[0].length) return null;
        Integer[] indexes = new Integer[output[0].length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }
        Arrays.sort(indexes, new Comparator<Integer>() {
            @Override
            public int compare(final Integer i1, final Integer i2) {
                return (false ? 1 : -1) * Float.compare(output[0][i1], output[0][i2]);
            }
        });
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = indexes[i].intValue();
        }
        return result;
    }

}
