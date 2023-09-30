package com.example.wallnut.config

class Config {

    object Config {
        private val properties: MutableMap<String, Any> = mutableMapOf()

        fun addProperty(key: String, value: Any) {
            properties[key] = value
        }

        fun getProperty(key: String): Any? {
            return properties[key]
        }
    }


}