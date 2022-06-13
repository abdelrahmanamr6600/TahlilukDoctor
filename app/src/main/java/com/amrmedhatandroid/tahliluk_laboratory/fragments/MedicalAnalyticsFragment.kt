package com.amrmedhatandroid.tahliluk_laboratory.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.amrmedhatandroid.tahliluk_laboratory.R
import com.amrmedhatandroid.tahliluk_laboratory.adapters.MedicalAnalyticsAdapter
import com.amrmedhatandroid.tahliluk_laboratory.database.PreferenceManager
import com.amrmedhatandroid.tahliluk_laboratory.databinding.FragmentMedicalAnalyticsBinding
import com.amrmedhatandroid.tahliluk_laboratory.models.Analytics
import com.amrmedhatandroid.tahliluk_laboratory.utilities.Constants
import com.amrmedhatandroid.tahliluk_laboratory.utilities.SupportClass
import com.amrmedhatandroid.tahliluk_laboratory.viewModels.MedicalAnalysisViewModel
import kotlinx.coroutines.flow.collect



class MedicalAnalyticsFragment : Fragment() {
    private lateinit var mFragmentMedicalAnalyticsBinding: FragmentMedicalAnalyticsBinding
    private lateinit var mMedicalAnalyticsViewModel: MedicalAnalysisViewModel
    private lateinit var mMedicalAnalyticsList: ArrayList<Analytics>
    private lateinit var mMedicalAnalyticsAdapter: MedicalAnalyticsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMedicalAnalyticsViewModel = ViewModelProvider(this)[MedicalAnalysisViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mFragmentMedicalAnalyticsBinding = FragmentMedicalAnalyticsBinding.inflate(layoutInflater)
        getMedicalAnalytics()
        setListeners()
        return mFragmentMedicalAnalyticsBinding.root
    }


    companion object {

        @JvmStatic
        fun newInstance() =
            MedicalAnalyticsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    private fun getMedicalAnalytics() {
        val preference = PreferenceManager(requireContext())
        val labId = preference.getString(Constants.KEY_LAB_ID)
        lifecycleScope.launchWhenResumed {
            mMedicalAnalyticsViewModel.getMedicalAnalysis(
                requireContext(),
                labId!!,
                Constants.KEY_COLLECTION_LABS
            ).collect {
                if (it.size > 0) {
                    mMedicalAnalyticsList = ArrayList()
                    mMedicalAnalyticsList = it

                    mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis.visibility = View.VISIBLE

                    mMedicalAnalyticsAdapter = MedicalAnalyticsAdapter(mMedicalAnalyticsList)
                    mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis.layoutManager =
                        LinearLayoutManager(requireContext())
                    mFragmentMedicalAnalyticsBinding.rvMedicalAnalysis.adapter =
                        mMedicalAnalyticsAdapter
                    SupportClass.loading(false,
                        null,
                        mFragmentMedicalAnalyticsBinding.progressBar)
                }
                }
            }
        }


    private fun setListeners(){
        mFragmentMedicalAnalyticsBinding.btnAddAnalysis.setOnClickListener {
            val addMedicalAnalysisFragment = AddMedicalAnalysisFragment.newInstance()
            val bundle = Bundle()
//            bundle.putParcelable(Constants.Reservation, reserve)
//            reservationDetailsFragment.arguments = bundle
            val fragmentManager: FragmentManager =
                (mFragmentMedicalAnalyticsBinding.root.context as FragmentActivity).supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.fui_slide_in_right,
                R.anim.fragmentanimation,
                R.anim.fui_slide_in_right,
                R.anim.fragmentanimation
            )
            fragmentTransaction.replace(R.id.fragment_container, addMedicalAnalysisFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    }
