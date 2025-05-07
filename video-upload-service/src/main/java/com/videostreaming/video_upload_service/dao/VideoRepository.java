package com.videostreaming.video_upload_service.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.videostreaming.video_upload_service.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	Optional<Video> findById(int id);

}
