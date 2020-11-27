package edu.utah.cs4530.emergency.extension

import androidx.navigation.NavOptions
import edu.utah.cs4530.emergency.R

fun NavOptions.Builder.default(): NavOptions.Builder = NavOptions.Builder()
    .setEnterAnim(R.anim.nav_default_enter_anim)
    .setExitAnim(R.anim.nav_default_exit_anim)
    .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
    .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
