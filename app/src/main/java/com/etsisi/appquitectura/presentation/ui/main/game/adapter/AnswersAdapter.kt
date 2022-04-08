package com.etsisi.appquitectura.presentation.ui.main.game.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.etsisi.appquitectura.databinding.ItemAnswerBinding
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseAdapter
import com.etsisi.appquitectura.presentation.common.BaseHolder
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.common.QuestionListener

class AnswersAdapter(
    private val listener: QuestionListener,
    private val questionBO: QuestionBO
): BaseAdapter<AnswerBO, AnswersAdapter.AnswerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerHolder {
        val view= ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnswerHolder(view)
    }

    inner class AnswerHolder(view: ItemAnswerBinding): BaseHolder<AnswerBO, ItemAnswerBinding>(view) {
        override fun bind(item: AnswerBO) {
            view.apply {
                questionBO = this@AnswersAdapter.questionBO
                listener = this@AnswersAdapter.listener
                answerBO = item
            }
        }

    }
}