package by.slavintodron.mytestappl.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import by.slavintodron.mytestappl.Person
import by.slavintodron.mytestappl.PersonSG
import by.slavintodron.mytestappl.databinding.FragmentInfoPersonBinding
import coil.load


class FragmentInfoPerson : Fragment() {
    private var _binding: FragmentInfoPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var textViewidd: TextView
    private lateinit var _idPerson: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null) {
            _idPerson =
                arguments?.let { FragmentInfoPersonArgs.fromBundle(it).idPerson }.toString()
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInfoPersonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val person: Person? = PersonSG.list?.find { it._id == _idPerson }

        if (person != null) {
            binding.textViewFruit.text = person.favoriteFruit
            binding.textViewInfoName.text = person.name
            binding.textViewInfoAge.text = person.age.toString()
            binding.textViewInfoGender.text = person.gender
            binding.textViewEye.text = person.eyeColor
            binding.textViewInd.text = person._id
            binding.textViewBalance.text = person.balance

            binding.imageViewPerson.load((person.picture)) {
                placeholder(android.R.drawable.stat_notify_sync)
                error(android.R.drawable.stat_notify_error)
            }
        }

    }

}