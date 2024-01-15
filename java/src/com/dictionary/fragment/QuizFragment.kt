package com.dictionary.fragment

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.DicQuizFragmentBinding
import com.core.base.BaseFragment
import com.core.extensions.empty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : BaseFragment<DicQuizFragmentBinding>(DicQuizFragmentBinding::inflate) {

    override fun onResume() {
        super.onResume()
        activity?.let {
            it as AppCompatActivity
            it.supportActionBar?.title = "Quiz of the day"
        }
    }


    override fun initUserInterface(view: View?) {
        val quizModel = preferenceManager.getQuizOfTheDay()
        quizModel?.let {
            val answers: MutableList<String> = quizModel.optionList.toMutableList()
            answers.add(quizModel.answer)
            var userAnswer = String.empty
            answers.shuffle()
            var selectedRadioButton :RadioButton = viewDataBinding.answer1
            viewDataBinding.apply {
                txtQuestion.text = quizModel.question
                answer1.text = answers[0]
                answer2.text = answers[1]
                answer3.text = answers[2]
                answer4.text = answers[3]

                answerOptionsRadioGroup.setOnCheckedChangeListener { p0, p1 ->
                   val radioButton =  p0.findViewById<RadioButton>(p1)
                    selectedRadioButton = radioButton
                    val index = p0.indexOfChild(radioButton)
                    userAnswer = answers[index]
                    btnSubmit.isEnabled = true
                }

                btnSubmit.setOnClickListener {
                    if (userAnswer == quizModel.answer) {
                        showToast("You choose correct answer")
                        selectedRadioButton.setTextColor(resources.getColor(R.color.primaryColor))
                    } else {
                        showToast("Wrong Answer")
                    }
                }

            }
        }

    }
}