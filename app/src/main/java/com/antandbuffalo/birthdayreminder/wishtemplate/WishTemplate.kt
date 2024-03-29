package com.antandbuffalo.birthdayreminder.wishtemplate

import android.app.Activity
import android.app.Fragment
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.antandbuffalo.birthdayreminder.Constants
import com.antandbuffalo.birthdayreminder.R
import com.antandbuffalo.birthdayreminder.Util
import com.antandbuffalo.birthdayreminder.settings.SettingsViewModel

class WishTemplate : Activity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wish_template)

        val settingsPreference = Util.getSharedPreference()
        val currentTemplate = settingsPreference.getString(Constants.PREFERENCE_WISH_TEMPLATE, Constants.WISH_TEMPLATE_DEFAULT)

        val save = findViewById<View>(R.id.save) as ImageButton
        save.setBackgroundResource(R.drawable.save_button)

        val cancel = findViewById<View>(R.id.cancel) as ImageButton
        cancel.setBackgroundResource(R.drawable.cancel_button)

        val editText = findViewById (R.id.wishTemplate1) as EditText
        editText.setText(currentTemplate)

        save.setOnClickListener {
            val editor = settingsPreference.edit()
            editor.putString(Constants.PREFERENCE_WISH_TEMPLATE, editText.text.toString())
            editor.commit()

            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        cancel.setOnClickListener {
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }
}
