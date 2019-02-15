package com.aerow.jira;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.aerow.config.Config;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

public class JIRARestTool {

	private static URI jiraServerUri = URI.create(Config.getInstance().getJiraUrl());

	private static Map<String, String> JIRA_REDMINE = new HashMap<>();

	static {
		JIRA_REDMINE.put("9", "9896");
		JIRA_REDMINE.put("10", "9895");
		JIRA_REDMINE.put("11", "9881");
		JIRA_REDMINE.put("12", "9880");
		JIRA_REDMINE.put("13", "9879");
		JIRA_REDMINE.put("14", "9878");
		JIRA_REDMINE.put("15", "9876");
		JIRA_REDMINE.put("16", "9860");
		JIRA_REDMINE.put("17", "9793");
		JIRA_REDMINE.put("18", "9751");
		JIRA_REDMINE.put("19", "9746");
		JIRA_REDMINE.put("20", "9739");
		JIRA_REDMINE.put("21", "9628");
		JIRA_REDMINE.put("22", "9615");
		JIRA_REDMINE.put("23", "9581");
		JIRA_REDMINE.put("24", "9580");
		JIRA_REDMINE.put("25", "9579");
		JIRA_REDMINE.put("26", "9570");
		JIRA_REDMINE.put("27", "9542");
		JIRA_REDMINE.put("28", "9537");
		JIRA_REDMINE.put("29", "9517");
		JIRA_REDMINE.put("30", "9480");
		JIRA_REDMINE.put("31", "9444");
		JIRA_REDMINE.put("32", "9436");
		JIRA_REDMINE.put("33", "9401");
		JIRA_REDMINE.put("34", "9388");
		JIRA_REDMINE.put("35", "9377");
		JIRA_REDMINE.put("36", "9370");
		JIRA_REDMINE.put("37", "9367");
		JIRA_REDMINE.put("38", "9357");
		JIRA_REDMINE.put("39", "9354");
		JIRA_REDMINE.put("40", "9353");
		JIRA_REDMINE.put("41", "9349");
		JIRA_REDMINE.put("42", "9346");
		JIRA_REDMINE.put("43", "9345");
		JIRA_REDMINE.put("44", "9341");
		JIRA_REDMINE.put("45", "9318");
		JIRA_REDMINE.put("46", "9317");
		JIRA_REDMINE.put("47", "9301");
		JIRA_REDMINE.put("48", "9300");
		JIRA_REDMINE.put("49", "9267");
		JIRA_REDMINE.put("50", "9252");
		JIRA_REDMINE.put("51", "9217");
		JIRA_REDMINE.put("52", "9189");
		JIRA_REDMINE.put("53", "9128");
		JIRA_REDMINE.put("54", "9053");
		JIRA_REDMINE.put("55", "9015");
		JIRA_REDMINE.put("56", "8980");
		JIRA_REDMINE.put("57", "8948");
		JIRA_REDMINE.put("58", "8935");
		JIRA_REDMINE.put("59", "8837");
		JIRA_REDMINE.put("60", "8822");
		JIRA_REDMINE.put("61", "8818");
		JIRA_REDMINE.put("62", "8792");
		JIRA_REDMINE.put("63", "8783");
		JIRA_REDMINE.put("64", "8776");
		JIRA_REDMINE.put("65", "8770");
		JIRA_REDMINE.put("66", "8651");
		JIRA_REDMINE.put("67", "8646");
		JIRA_REDMINE.put("68", "8585");
		JIRA_REDMINE.put("69", "8578");
		JIRA_REDMINE.put("70", "8577");
		JIRA_REDMINE.put("71", "8576");
		JIRA_REDMINE.put("72", "8575");
		JIRA_REDMINE.put("73", "8574");
		JIRA_REDMINE.put("74", "8573");
		JIRA_REDMINE.put("75", "8569");
		JIRA_REDMINE.put("76", "8566");
		JIRA_REDMINE.put("77", "8564");
		JIRA_REDMINE.put("78", "8513");
		JIRA_REDMINE.put("79", "8497");
		JIRA_REDMINE.put("80", "8433");
		JIRA_REDMINE.put("81", "8414");
		JIRA_REDMINE.put("82", "8410");
		JIRA_REDMINE.put("83", "8394");
		JIRA_REDMINE.put("84", "8375");
		JIRA_REDMINE.put("85", "8344");
		JIRA_REDMINE.put("86", "8321");
		JIRA_REDMINE.put("87", "8320");
		JIRA_REDMINE.put("88", "8315");
		JIRA_REDMINE.put("89", "8304");
		JIRA_REDMINE.put("90", "8286");
		JIRA_REDMINE.put("91", "8285");
		JIRA_REDMINE.put("92", "8283");
		JIRA_REDMINE.put("93", "8271");
		JIRA_REDMINE.put("94", "8252");
		JIRA_REDMINE.put("95", "8233");
		JIRA_REDMINE.put("96", "8164");
		JIRA_REDMINE.put("97", "8156");
		JIRA_REDMINE.put("98", "8123");
		JIRA_REDMINE.put("99", "8110");
		JIRA_REDMINE.put("100", "8023");
		JIRA_REDMINE.put("101", "8011");
		JIRA_REDMINE.put("102", "8009");
		JIRA_REDMINE.put("103", "7998");
		JIRA_REDMINE.put("104", "7990");
		JIRA_REDMINE.put("105", "7986");
		JIRA_REDMINE.put("106", "7963");
		JIRA_REDMINE.put("107", "7957");
		JIRA_REDMINE.put("108", "7954");
		JIRA_REDMINE.put("109", "7948");
		JIRA_REDMINE.put("110", "7946");
		JIRA_REDMINE.put("111", "7941");
		JIRA_REDMINE.put("112", "7940");
		JIRA_REDMINE.put("113", "7939");
		JIRA_REDMINE.put("114", "7938");
		JIRA_REDMINE.put("115", "7937");
		JIRA_REDMINE.put("116", "7936");
		JIRA_REDMINE.put("117", "7935");
		JIRA_REDMINE.put("118", "7934");
		JIRA_REDMINE.put("119", "7933");
		JIRA_REDMINE.put("120", "7924");
		JIRA_REDMINE.put("121", "7908");
		JIRA_REDMINE.put("122", "7837");
		JIRA_REDMINE.put("123", "7826");
		JIRA_REDMINE.put("124", "7781");
		JIRA_REDMINE.put("125", "7754");
		JIRA_REDMINE.put("126", "7751");
		JIRA_REDMINE.put("127", "7745");
		JIRA_REDMINE.put("128", "7744");
		JIRA_REDMINE.put("129", "7738");
		JIRA_REDMINE.put("130", "7725");
		JIRA_REDMINE.put("131", "7715");
		JIRA_REDMINE.put("132", "7689");
		JIRA_REDMINE.put("133", "7682");
		JIRA_REDMINE.put("134", "7681");
		JIRA_REDMINE.put("135", "7652");
		JIRA_REDMINE.put("136", "7646");
		JIRA_REDMINE.put("137", "7640");
		JIRA_REDMINE.put("138", "7608");
		JIRA_REDMINE.put("139", "7603");
		JIRA_REDMINE.put("140", "7599");
		JIRA_REDMINE.put("141", "7598");
		JIRA_REDMINE.put("142", "7595");
		JIRA_REDMINE.put("143", "7576");
		JIRA_REDMINE.put("144", "7575");
		JIRA_REDMINE.put("145", "7570");
		JIRA_REDMINE.put("146", "7564");
		JIRA_REDMINE.put("147", "7557");
		JIRA_REDMINE.put("148", "7539");
		JIRA_REDMINE.put("149", "7533");
		JIRA_REDMINE.put("150", "7532");
		JIRA_REDMINE.put("151", "7531");
		JIRA_REDMINE.put("152", "7530");
		JIRA_REDMINE.put("153", "7529");
		JIRA_REDMINE.put("154", "7526");
		JIRA_REDMINE.put("155", "7524");
		JIRA_REDMINE.put("156", "7513");
		JIRA_REDMINE.put("157", "7452");
		JIRA_REDMINE.put("158", "7451");
		JIRA_REDMINE.put("159", "7444");
		JIRA_REDMINE.put("160", "7441");
		JIRA_REDMINE.put("161", "7421");
		JIRA_REDMINE.put("162", "7417");
		JIRA_REDMINE.put("163", "7393");
		JIRA_REDMINE.put("164", "7386");
		JIRA_REDMINE.put("165", "7361");
		JIRA_REDMINE.put("166", "7331");
		JIRA_REDMINE.put("167", "7316");
		JIRA_REDMINE.put("168", "7294");
		JIRA_REDMINE.put("169", "7284");
		JIRA_REDMINE.put("170", "7280");
		JIRA_REDMINE.put("171", "7268");
		JIRA_REDMINE.put("172", "7266");
		JIRA_REDMINE.put("173", "7257");
		JIRA_REDMINE.put("174", "7254");
		JIRA_REDMINE.put("175", "7252");
		JIRA_REDMINE.put("176", "7200");
		JIRA_REDMINE.put("177", "7199");
		JIRA_REDMINE.put("178", "7195");
		JIRA_REDMINE.put("179", "7190");
		JIRA_REDMINE.put("180", "7185");
		JIRA_REDMINE.put("181", "7162");
		JIRA_REDMINE.put("182", "7140");
		JIRA_REDMINE.put("183", "7129");
		JIRA_REDMINE.put("184", "7127");
		JIRA_REDMINE.put("185", "7095");
		JIRA_REDMINE.put("186", "7093");
		JIRA_REDMINE.put("187", "7084");
		JIRA_REDMINE.put("188", "7037");
		JIRA_REDMINE.put("189", "7028");
		JIRA_REDMINE.put("190", "7027");
		JIRA_REDMINE.put("191", "6953");
		JIRA_REDMINE.put("192", "6950");
		JIRA_REDMINE.put("193", "6948");
		JIRA_REDMINE.put("194", "6910");
		JIRA_REDMINE.put("195", "6872");
		JIRA_REDMINE.put("196", "6871");
		JIRA_REDMINE.put("197", "6855");
		JIRA_REDMINE.put("198", "6853");
		JIRA_REDMINE.put("199", "6830");
		JIRA_REDMINE.put("200", "6824");
		JIRA_REDMINE.put("201", "6818");
		JIRA_REDMINE.put("202", "6796");
		JIRA_REDMINE.put("203", "6719");
		JIRA_REDMINE.put("204", "6696");
		JIRA_REDMINE.put("205", "6691");
		JIRA_REDMINE.put("206", "6679");
		JIRA_REDMINE.put("207", "6678");
		JIRA_REDMINE.put("208", "6667");
		JIRA_REDMINE.put("209", "6664");
		JIRA_REDMINE.put("210", "6645");
		JIRA_REDMINE.put("211", "6644");
		JIRA_REDMINE.put("212", "6637");
		JIRA_REDMINE.put("213", "6634");
		JIRA_REDMINE.put("214", "6633");
		JIRA_REDMINE.put("215", "6619");
		JIRA_REDMINE.put("216", "6615");
		JIRA_REDMINE.put("217", "6614");
		JIRA_REDMINE.put("218", "6612");
		JIRA_REDMINE.put("219", "6607");
		JIRA_REDMINE.put("220", "6606");
		JIRA_REDMINE.put("221", "6552");
		JIRA_REDMINE.put("222", "6551");
		JIRA_REDMINE.put("223", "6530");
		JIRA_REDMINE.put("224", "6521");
		JIRA_REDMINE.put("225", "6516");
		JIRA_REDMINE.put("226", "6515");
		JIRA_REDMINE.put("227", "6514");
		JIRA_REDMINE.put("228", "6482");
		JIRA_REDMINE.put("229", "6442");
		JIRA_REDMINE.put("230", "6439");
		JIRA_REDMINE.put("231", "6407");
		JIRA_REDMINE.put("232", "6388");
		JIRA_REDMINE.put("233", "6385");
		JIRA_REDMINE.put("234", "6382");
		JIRA_REDMINE.put("235", "6371");
		JIRA_REDMINE.put("236", "6368");
		JIRA_REDMINE.put("237", "6367");
		JIRA_REDMINE.put("238", "6361");
		JIRA_REDMINE.put("239", "6318");
		JIRA_REDMINE.put("240", "6301");
		JIRA_REDMINE.put("241", "6288");
		JIRA_REDMINE.put("242", "6286");
		JIRA_REDMINE.put("243", "6285");
		JIRA_REDMINE.put("244", "6283");
		JIRA_REDMINE.put("245", "6282");
		JIRA_REDMINE.put("246", "6281");
		JIRA_REDMINE.put("247", "6278");
		JIRA_REDMINE.put("248", "6274");
		JIRA_REDMINE.put("249", "6272");
		JIRA_REDMINE.put("250", "6255");
		JIRA_REDMINE.put("251", "6247");
		JIRA_REDMINE.put("252", "6242");
		JIRA_REDMINE.put("253", "6241");
		JIRA_REDMINE.put("254", "6234");
		JIRA_REDMINE.put("255", "6229");
		JIRA_REDMINE.put("256", "6228");
		JIRA_REDMINE.put("257", "6227");
		JIRA_REDMINE.put("258", "6226");
		JIRA_REDMINE.put("259", "6225");
		JIRA_REDMINE.put("260", "6221");
		JIRA_REDMINE.put("261", "6188");
		JIRA_REDMINE.put("262", "6187");
		JIRA_REDMINE.put("263", "6186");
		JIRA_REDMINE.put("264", "6185");
		JIRA_REDMINE.put("265", "6184");
		JIRA_REDMINE.put("266", "6183");
		JIRA_REDMINE.put("267", "6168");
		JIRA_REDMINE.put("268", "6167");
		JIRA_REDMINE.put("269", "6166");
		JIRA_REDMINE.put("270", "6106");
		JIRA_REDMINE.put("271", "6102");
		JIRA_REDMINE.put("272", "6089");
		JIRA_REDMINE.put("273", "6064");
		JIRA_REDMINE.put("274", "6028");
		JIRA_REDMINE.put("275", "6027");
		JIRA_REDMINE.put("276", "6026");
		JIRA_REDMINE.put("277", "6000");
		JIRA_REDMINE.put("278", "5967");
		JIRA_REDMINE.put("279", "5954");
		JIRA_REDMINE.put("280", "5950");
		JIRA_REDMINE.put("281", "5900");
		JIRA_REDMINE.put("282", "5869");
		JIRA_REDMINE.put("283", "5868");
		JIRA_REDMINE.put("284", "5867");
		JIRA_REDMINE.put("285", "5866");
		JIRA_REDMINE.put("286", "5865");
		JIRA_REDMINE.put("287", "5864");
		JIRA_REDMINE.put("288", "5863");
		JIRA_REDMINE.put("289", "5862");
		JIRA_REDMINE.put("290", "5861");
		JIRA_REDMINE.put("291", "5860");
		JIRA_REDMINE.put("292", "5859");
		JIRA_REDMINE.put("293", "5858");
		JIRA_REDMINE.put("294", "5856");
		JIRA_REDMINE.put("295", "5855");
		JIRA_REDMINE.put("296", "5854");
		JIRA_REDMINE.put("297", "5852");
		JIRA_REDMINE.put("298", "5850");
		JIRA_REDMINE.put("299", "5849");
		JIRA_REDMINE.put("300", "5848");
		JIRA_REDMINE.put("301", "5847");
		JIRA_REDMINE.put("302", "5846");
		JIRA_REDMINE.put("303", "5845");
		JIRA_REDMINE.put("304", "5844");
		JIRA_REDMINE.put("305", "5843");
		JIRA_REDMINE.put("306", "5821");
		JIRA_REDMINE.put("307", "5659");
		JIRA_REDMINE.put("308", "5656");
		JIRA_REDMINE.put("309", "5616");
		JIRA_REDMINE.put("310", "5605");
		JIRA_REDMINE.put("311", "5604");
		JIRA_REDMINE.put("312", "5588");
		JIRA_REDMINE.put("313", "5554");
		JIRA_REDMINE.put("314", "5539");
		JIRA_REDMINE.put("315", "5521");
		JIRA_REDMINE.put("316", "5516");
		JIRA_REDMINE.put("317", "5506");
		JIRA_REDMINE.put("318", "5505");
		JIRA_REDMINE.put("319", "5498");
		JIRA_REDMINE.put("320", "5485");
		JIRA_REDMINE.put("321", "5467");
	}

	public static void main(String[] args) throws IOException {
		final AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
		final JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, Config.getInstance().getJiraLogin(), Config.getInstance().getJiraPassword());

		try {
			final IssueRestClient issueClient = restClient.getIssueClient();

			File exportFolder = Config.getInstance().getExportFolder();

			/*for (File folder : exportFolder.listFiles()) {
				if (folder.isDirectory())
				{
					String redmineId = folder.getName();
					final SearchResult searchResult = restClient.getSearchClient().searchJql("\"[MIGR] Issue ID\" ~ \""+redmineId+"\"").claim();
					System.out.println(redmineId);
					for (Issue issue : searchResult.getIssues()) {
						Promise<Void> attach = issueClient.addAttachments(issue.getAttachmentsUri(), folder.listFiles());
						attach.claim();
					}
				}
			}*/

			//			for (String jiraKey : JIRA_REDMINE.keySet()) {
			//				File redmine = new File(exportFolder.getPath() + File.separator + JIRA_REDMINE.get(jiraKey));
			//				if (redmine.exists() && redmine.isDirectory())
			//				{
			//					final SearchResult searchResult = restClient.getSearchClient().searchJql("issuekey = \"DOCCONNECT-"+jiraKey+"\"").claim();
			//					System.out.println(JIRA_REDMINE.get(jiraKey));
			//					for (Issue issue : searchResult.getIssues()) {
			//						Promise<Void> attach = issueClient.addAttachments(issue.getAttachmentsUri(), redmine.listFiles());
			//						attach.claim();
			//					}
			//				
			//						
			//					Files.move(redmine, new File(exportFolder.getPath() + File.separator + "Completed" +"/"+ JIRA_REDMINE.get(jiraKey)));
			//				}
			//				
			//			}

			Map<String, String> map = new HashMap<>();
			map.put("New", "10020");
			map.put("Analysis in progress" , "3");
			map.put("Taken into account" ,"3");
			map.put("Cancelled" ,"10024");
			map.put("Rejected" ,"10025");
			map.put("Suspended" , "10003");
			map.put("Terminated" , "6");
			map.put("Processed" , "5");

			Reader in = new FileReader("C:\\temp\\redmine\\schneider\\redmine_no_comment_5500982355397879819.csv");
			final CSVParser parser = new CSVParser(in, CSVFormat.EXCEL.withHeader());
			for (CSVRecord record : parser) {
				String jiraKey = record.get("jira_id");
				String status = record.get("status");
				Map<String, FieldInput> fields = new HashMap<>();
				fields.put("status", new FieldInput("status", map.get(status)));
				IssueInput issueType = new IssueInput(fields);
				issueClient.updateIssue("DOCCONNECT-"+jiraKey, issueType).claim();
			}
			
			parser.close();


		} finally {
			restClient.close();
		}
	}
}
