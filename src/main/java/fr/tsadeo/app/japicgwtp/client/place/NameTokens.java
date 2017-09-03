/*
 * Copyright 2011 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package fr.tsadeo.app.japicgwtp.client.place;

/**
 * Main presenters name tokens.
 */
public class NameTokens {
	
	public enum Token {
		admin,
		settings,
		site,
		search,
		home;
		
	}
	
	public static final String admin = "/admin";

	public static final String settings = "/settings";

	public static final String site = "/site";

	public static final String search = "/search";

	public static final String home = "/home";

	public static String getHome() {
		return home;
	}

	public static String getSearch() {
		return search;
	}

	public static String getSite() {
		return site;
	}

	public static String getSettings() {
		return settings;
	}

	public static String getAdmin() {
		return admin;
	}
}
