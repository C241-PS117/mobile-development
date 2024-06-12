package com.example.essy.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.essy.data.model.TeamMember
import com.example.essy.databinding.ListTeamMemberBinding

class TeamAdapter(private val team: List<TeamMember>) : RecyclerView.Adapter<TeamAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListTeamMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = team[position]
        holder.bind(member)
    }

    override fun getItemCount(): Int = team.size

    inner class ViewHolder(private val binding: ListTeamMemberBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageLinkedin.setOnClickListener {
                val linkedinIntent = Intent(Intent.ACTION_VIEW, Uri.parse(team[adapterPosition].linkedinUrl))
                binding.root.context.startActivity(linkedinIntent)
            }

            binding.imageInstagram.setOnClickListener {
                val instagramIntent = Intent(Intent.ACTION_VIEW, Uri.parse(team[adapterPosition].instagramUrl))
                binding.root.context.startActivity(instagramIntent)
            }
        }

        fun bind(member: TeamMember) {
            binding.apply {
                txtUsernameCard.text = member.name
                txtEmailCard.text = member.email
                circleProfile.setImageResource(member.photoResId)
                // Glide.with(itemView).load(member.photoUrl).into(ivPhoto)
            }
        }
    }
}