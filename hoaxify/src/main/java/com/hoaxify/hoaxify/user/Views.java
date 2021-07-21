package com.hoaxify.hoaxify.user;

// this class contains only interfaces for defining view type
public class Views {

	public interface Base{}
	
	// can have hierarchical relation between these interfaces
	// By extending, fields tagged with Base are also going to be serialized in Sensitive view
	public interface Sensitive extends Base{}
	
}
