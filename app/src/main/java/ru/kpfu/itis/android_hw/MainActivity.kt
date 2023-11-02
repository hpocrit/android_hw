package ru.kpfu.itis.android_hw

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.Transition
import android.transition.TransitionSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.container, StartFragment())
            .commit()
    }
    fun showDetail(view: View, id : Int) {
        val transitionName = view.transitionName
        val fragment = NewsDetailsFragment.newInstance(id, transitionName)
        fragment.sharedElementEnterTransition = getTransition()
        fragment.sharedElementReturnTransition = getTransition()
        supportFragmentManager
            .beginTransaction()
            .addSharedElement(view, transitionName)
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun getTransition(): Transition {
        val set = TransitionSet()
        set.ordering = TransitionSet.ORDERING_TOGETHER
        set.addTransition(ChangeBounds())
        set.addTransition(ChangeImageTransform())
        set.addTransition(ChangeTransform())
        return set
    }


}
