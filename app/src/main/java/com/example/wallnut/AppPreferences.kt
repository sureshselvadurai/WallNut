import android.content.Context

object AppPreferences {
    private const val PREFS_NAME = "MyAppPrefs"
    private const val KEY_FIRST_TIME = "isFirstTime"

    fun setFirstTimeUser(context: Context, isFirstTime: Boolean) {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(KEY_FIRST_TIME, isFirstTime).apply()
    }

    fun isFirstTimeUser(context: Context): Boolean {
        val sharedPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(KEY_FIRST_TIME, true)
    }
}
