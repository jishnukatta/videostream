package com.videostreaming.transcoding_service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.videostreaming.transcoding_service.entity.Video;
import com.videostreaming.transcoding_service.entity.VideoStatus;

public interface VideoRepository  extends JpaRepository<Video, Long>{

	List<Video> findByStatus(VideoStatus failed);

}
