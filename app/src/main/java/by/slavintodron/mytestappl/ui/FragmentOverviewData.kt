package by.slavintodron.mytestappl.ui

import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.slavintodron.mytestappl.*
import by.slavintodron.mytestappl.databinding.FragmentOverviewDataBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

private const val BASE_URL =
    "https://www.json-generator.com/api/json/get/clyWAUbaxu?indent=2"

class FragmentOverviewData : Fragment(), CustomRecyclerAdapter.OnItemClickListener  {
    private var isLinearLayoutManager = true
    private var optionsMenu: Menu? = null
    private lateinit var recyclerView: RecyclerView

    private var _binding: FragmentOverviewDataBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recyclerView
        if (PersonSG.list != null){
            filterDataAndSetToRV()
        } else
        { asyncGetJsonData()}
    }
    private fun chooseLayout() {
            if (isLinearLayoutManager) {
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
            binding.recyclerView.adapter = PersonSG.list?.let { CustomRecyclerAdapter(it, this) }
    }
    private fun sortByAgeSetIcon(menuItem: MenuItem?){
        if (menuItem == null)
            return
        if (PersonSG.sortedByAge) {
            if (PersonSG.sortAgeAscend) {
        menuItem.title = getString(R.string.sort_age)+getString(R.string.sort_ascend)}
            else
            {
                menuItem.title = getString(R.string.sort_age)+getString(R.string.sort_descend)}
        }
    }
    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return

        menuItem.icon =
                if (isLinearLayoutManager)
                    context?.let { ContextCompat.getDrawable(it, android.R.drawable.ic_menu_sort_by_size) }
                else context?.let { ContextCompat.getDrawable(it, android.R.drawable.ic_menu_today) }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_layout, menu)
        optionsMenu = menu
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager

                filterDataAndSetToRV()
                setIcon(item)
                return true
            }
            R.id.action_switch_filter_age -> {
                PersonSG.sortedByAge=true
                PersonSG.sortAgeAscend=!PersonSG.sortAgeAscend
                sortByAgeSetIcon(item)
                filterDataAndSetToRV()
                return true
            }
            R.id.app_bar_switch_gender -> {
                item.isChecked = !item.isChecked
                PersonSG.sortedByGender = item.isChecked

                optionsMenu?.findItem(R.id.app_bar_switch_gender_f)?.isEnabled = item.isChecked
                optionsMenu?.findItem(R.id.app_bar_switch_gender_m)?.isEnabled = item.isChecked
                if (item.isChecked) {
                    optionsMenu?.findItem(R.id.app_bar_switch_gender_m)?.isChecked = true
                    PersonSG.sortGenderIsMale = true
                }
                else
                {
                    optionsMenu?.findItem(R.id.app_bar_switch_gender_m)?.isChecked = false
                    optionsMenu?.findItem(R.id.app_bar_switch_gender_f)?.isChecked = false
                }
                filterDataAndSetToRV()
                return true
            }
            R.id.app_bar_switch_gender_m -> {
                item.isChecked = true
                PersonSG.sortGenderIsMale = true
                filterDataAndSetToRV()

                return true
            }
            R.id.app_bar_switch_gender_f -> {
                item.isChecked = true
                PersonSG.sortGenderIsMale = false
                filterDataAndSetToRV()
                return true
            }
            R.id.action_switch_refresh -> {
                PersonSG.sortedByAge = false
                PersonSG.sortedByGender = false
                PersonSG.list = ArrayList( )

                optionsMenu?.findItem(R.id.action_switch_filter_age)?.title = getString(R.string.sort_age)
                optionsMenu?.findItem(R.id.app_bar_switch_gender)?.isChecked = false
                optionsMenu?.findItem(R.id.app_bar_switch_gender_f)?.isChecked = false
                optionsMenu?.findItem(R.id.app_bar_switch_gender_m)?.isChecked = false

                binding.textViewError.visibility = View.GONE
                binding.recyclerView.adapter = CustomRecyclerAdapter(ArrayList<Person>(), this)
                binding.progressBar.visibility = View.VISIBLE

                asyncGetJsonData()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterListByAge(ListToFiltered:ArrayList<Person>):ArrayList<Person>{
        if (PersonSG.sortedByAge){
            when  (PersonSG.sortAgeAscend){
                true -> { ListToFiltered.sortBy { it.age }}
                false -> { ListToFiltered.sortBy { it.age }
                    ListToFiltered.reverse()
                }
            }
            return ListToFiltered
        }
        else return ListToFiltered
    }

    private fun filterListByGender(ListToFiltered:ArrayList<Person>):ArrayList<Person>{
        var tmpList: ArrayList<Person> = ArrayList()
        if (PersonSG.sortedByGender){
            when  (PersonSG.sortGenderIsMale){
                true -> { tmpList = ListToFiltered.filter { it.gender =="male" } as ArrayList<Person>
                }
                false -> { tmpList = ListToFiltered.filter { it.gender =="female" } as ArrayList<Person>
                }
            }
            return tmpList
        } else
            return ListToFiltered
    }
    private fun filterDataAndSetToRV ()
    {
        chooseLayout()
        var list: ArrayList<Person>? =  PersonSG.list
        if (list != null) {
            list = filterListByAge(list)
        }
        if (list != null) {
            list = filterListByGender(list)
        }
        binding.recyclerView.adapter = list?.let { CustomRecyclerAdapter(it, this) }
    }
    override fun onItemClick(idPerson: String) {
        val action = FragmentOverviewDataDirections.actionFragmentOverviewDataToFragmentInfoPerson(idPerson)
        findNavController(this).navigate(action)
      }

    private fun asyncGetJsonData(){
        class AsyncRequest : AsyncTask<String?, Int?, Boolean>() {

            override fun onPreExecute() {
                  binding.progressBar.visibility = View.VISIBLE
            }

            override fun onPostExecute(execResult: Boolean) {
                if (execResult){
                    setErrorMessage(View.GONE)
                    filterDataAndSetToRV()
                } else
                {
                    setErrorMessage(View.VISIBLE)
                }
                  binding.progressBar.visibility = View.GONE
            }

            override fun doInBackground(vararg params: String?): Boolean {

                try {
                    val apiResponse = URL(BASE_URL).readText()
                    val listPersonType = object : TypeToken<List<Person>>() {}.type

                    PersonSG.list = Gson().fromJson(getJsonDataFromURL(apiResponse), listPersonType)

                } catch (e: Exception) {
                    return false
                }
                return true
            }
        }
        AsyncRequest().execute()
    }

    private fun setErrorMessage(visible:Int){
        binding.textViewError.visibility =visible
    }
}