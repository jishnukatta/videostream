package com.videostreaming.video_streaming_service.dto;

public enum SubscriptionLevel {

	 NORMAL, PREMIUM, VIP;
	 
	    public boolean canAccess(SubscriptionLevel videoLevel) {
	        // Higher or equal level can access lower or equal video
	        return this.ordinal() >= videoLevel.ordinal();
	    }

	    public static SubscriptionLevel fromString(String category) {
	        try {
	            return SubscriptionLevel.valueOf(category.toUpperCase());
	        } catch (IllegalArgumentException | NullPointerException e) {
	            return null;
	        }
	    }

}
