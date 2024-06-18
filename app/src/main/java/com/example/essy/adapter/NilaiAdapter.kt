package com.example.essy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.essy.data.model.NilaiResult
import com.example.essy.databinding.ListJawabanBinding

class NilaiAdapter(
    private var nilaiList: List<NilaiResult>
) : RecyclerView.Adapter<NilaiAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListJawabanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nilai = nilaiList[position]
        holder.bind(nilai)
    }

    override fun getItemCount(): Int = nilaiList.size

    fun setData(newNilaiList: List<NilaiResult>) {
        nilaiList = newNilaiList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListJawabanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nilai: NilaiResult) {
            binding.apply {
                txtQuestion.text = nilai.namaSiswa
                txtAnswer.text = nilai.nilaiSiswa.toString()
            }
        }
    }
}
