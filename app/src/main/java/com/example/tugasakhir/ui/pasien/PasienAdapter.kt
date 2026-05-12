package com.example.tugasakhir.ui.pasien

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhir.data.model.Pasien
import com.example.tugasakhir.databinding.ItemPasienBinding

class PasienAdapter : ListAdapter<Pasien, PasienAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPasienBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPasienBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pasien: Pasien) {
            binding.tvNama.text = pasien.nama
            binding.tvTanggalLahir.text = pasien.tanggal_lahir
            binding.tvJenisKelamin.text = if (pasien.jenis_kelamin == "L") "Laki-laki" else "Perempuan"
            binding.tvAlamat.text = pasien.alamat
            binding.tvNoTelepon.text = pasien.no_telepon
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Pasien>() {
        override fun areItemsTheSame(oldItem: Pasien, newItem: Pasien): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pasien, newItem: Pasien): Boolean {
            return oldItem == newItem
        }
    }
}
