package by.slavintodron.mytestappl

import android.content.Context
import java.io.IOException

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString

}
fun getJsonDataFromURL(path: String): String?{
    val jsonString: String
    try {
        jsonString = path
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }
    return jsonString
}