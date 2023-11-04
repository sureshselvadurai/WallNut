package com.example.wallnut.utils

import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Utility class for common file operations.
 */
class Utils {

    companion object {

        /**
         * Capitalizes the first letter of the input string.
         *
         * @param input The input string.
         * @return The input string with the first letter capitalized.
         */
        private fun capitalizeFirstLetter(input: String) = input.take(1).uppercase() + input.drop(1)

        /**
         * Converts a camelCase string to a space-separated string with the first letter capitalized.
         *
         * @param input The camelCase string to convert.
         * @return The space-separated string with the first letter capitalized.
         */
        fun camelCaseToSpaceSeparated(input: String): String {
            val result = StringBuilder()
            for (char in input) {
                if (char.isUpperCase() && result.isNotEmpty()) {
                    result.append(' ') // Add a space before uppercase characters (except at the beginning)
                }
                result.append(char.lowercaseChar())
            }
            return capitalizeFirstLetter(result.toString())
        }

        /**
         * Reads the content of a file located at the given file path.
         *
         * @param filePath The path to the file to be read.
         * @return The content of the file as a string, or null if an error occurs.
         */
        fun readFile(filePath: String): String? {
            var fileContents: String?
            val file = File(filePath)

            try {
                val inputStream = FileInputStream(file)
                val length = file.length().toInt()
                val buffer = ByteArray(length)

                inputStream.read(buffer)
                fileContents = String(buffer)

                inputStream.close()
            } catch (e: IOException) {
                fileContents = null
            }
            return fileContents
        }
    }
}
