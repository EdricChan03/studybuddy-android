package com.edricchan.studybuddy.interfaces;

/**
 * An interface for {@link com.edricchan.studybuddy.HelpActivity}
 */
public class HelpFeatured {
	private String helpTxt;
	private String helpUrl;
	public HelpFeatured(String helpTxt, String helpUrl) {
		this.helpTxt = helpTxt;
		this.helpUrl = helpUrl;
	}

	public String getHelpTxt() {
		return helpTxt;
	}

	public String getHelpUrl() {
		return helpUrl;
	}
}
