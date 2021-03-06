package com.etsisi.appquitectura.presentation.ui.main.game.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.ui.main.game.view.QuestionFragment

class QuestionsViewPagerAdapter(val fragment: Fragment): FragmentStateAdapter(fragment) {

    private var questionsList = mutableListOf<QuestionBO>()


    fun addData(list: List<QuestionBO>, positionStart: Int) {
        questionsList.clear()
        questionsList.addAll(list)
        notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun getItemCount(): Int = questionsList.size

    override fun createFragment(position: Int): Fragment {
        return QuestionFragment.newInstance(questionsList[position], fragment as? GameListener)
    }

}