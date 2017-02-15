package kr.ac.jbnu.ssel.misrac.ui.preference;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.runtime.FileLocator;

import kr.ac.jbnu.ssel.misrac.ui.view.Constant;
import test.kr.ac.jbnu.ssel.misrac.rule.testC.CCode;

public class MisraUIdataHandler {

	private static MisraUIdataHandler instance;

	private List<Rule> ruleList;
	private HashMap<String, List<Rule>> rulesByCategory = new HashMap<String, List<Rule>>();

	private MisraUIdataHandler() {
		try {
			if (ruleList == null) {
				loadAllRules();
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static MisraUIdataHandler getInstance() {
		if (instance == null) {
			instance = new MisraUIdataHandler();
		}

		return instance;
	}
	
	public HashSet<Rule> getShouldCheckRules()
	{
		HashSet<Rule> shouldCheckRules = new HashSet<Rule>();
		for (Rule rule : ruleList) {
			if( rule.isShouldCheck())
			{
				shouldCheckRules.add(rule);
			}
		}
		return shouldCheckRules;
	}

	public List<Rule> getRules() {
		return ruleList;
	}

	public List<Rule> getRules(String category) throws JAXBException {
		return rulesByCategory.get(category);
	}

	public List<Rule> loadAllRules() throws JAXBException {
		File file = new File(Constant.dataPath);
		// IWorkspace workspace= ResourcesPlugin.getWorkspace();
		// IPath location= Path.fromOSString(file.getAbsolutePath());
		// IFile ifile= workspace.getRoot().getFileForLocation(location);
		// file = ifile.getRawLocation().makeAbsolute().toFile();

		JAXBContext jc = JAXBContext.newInstance(Rules.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Rules rules = (Rules) unmarshaller.unmarshal(file);
		ruleList = rules.getRule();

		for (Rule rule : ruleList) {
			if (rule.getCategory() != null) {
				String category = rule.getCategory();
				List<Rule> ruleCate = rulesByCategory.get(category);

				if (ruleCate == null) {
					ruleCate = new ArrayList<Rule>();
					rulesByCategory.put(category, ruleCate);
				}

				ruleCate.add(rule);
			}
		}

		return ruleList;
	}

	/**
	 * TO BE DPRECATED!
	 * 
	 * @param ruleNumber
	 * @return
	 */
	public String getCode(String ruleNumber) {
		String code = null;
		String[] ruleNumArry = ruleNumber.split("Rule");
		String minerNum = ruleNumArry[1];
		String ruleClassWithPackage = null;
		// 1. Compare between ruleNumber of index in the table and ruleNumber in
		// the C code
		// 2. get code if the number match number of index of table
		String[] cCodeFiles = getCodeList();

		return code;
	}

	/**
	 * TO BE DPRECATED!
	 * 
	 * @return
	 */
	private String[] getCodeList() {
		String[] cCodeFiles = null;
		ClassLoader loader = CCode.class.getClassLoader();
		URL ruleCodeClassDictory = loader.getResource(CCode.class.getPackage().getName().replace('.', '/'));
		try {
			URL fileURL = FileLocator.toFileURL(ruleCodeClassDictory);
			File codeDicFile = new File(fileURL.toURI());
			cCodeFiles = codeDicFile.list();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return cCodeFiles;
	}

	/**
	 * TO BE DPRECATED!
	 * 
	 * @param cCodeClass
	 * @return
	 */
	private String getcClassRuleNumber(String cCodeClass) {
		StringBuffer wholeRuleNumber = new StringBuffer();
		String ruleNum = cCodeClass.substring(6);
		String[] removedClass = ruleNum.split(".C");
		String[] subStrings = removedClass[0].split("_");
		String startOfNum = null;
		if (subStrings[0].startsWith("0")) {
			startOfNum = subStrings[0].replace("0", "");
		} else {
			startOfNum = subStrings[0];
		}
		if (startOfNum != null) {
			String endOfNum = subStrings[1];
			wholeRuleNumber.append(startOfNum);
			wholeRuleNumber.append("." + endOfNum);
		}
		return wholeRuleNumber.toString();
	}

	public static void main(String[] args) throws JAXBException {
		MisraUIdataHandler misraUIdataHandler = new MisraUIdataHandler();
		List<Rule> ruleList = misraUIdataHandler.loadAllRules();
		for (Rule rule : ruleList) {
			System.out.println(rule.getMinerNum());
		}
	}

	public List<Rule> loadRules(String category) throws JAXBException {
		List<Rule> subRules = new ArrayList<Rule>();
		File file = new File(Constant.dataPath);
		JAXBContext jc = JAXBContext.newInstance(Rules.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Rules rules = (Rules) unmarshaller.unmarshal(file);
		for (Rule rule : rules.getRule()) {
			if (rule.getCategory() != null) {
				if (rule.getCategory().equals(category)) {
					subRules.add(rule);
				}
			}
		}
		return subRules;
	}
}
