import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.opencsv.CSVReader;

public class MyDatabase {

	final static int NUM_OF_FILES = 11;
	final static String CSV_FILE_NAME = "PHARMA_TRIALS_1000B.csv";
	final static String DATA_FILE_NAME = "data.db";

	final static byte double_blind_mask = 8; // binary 0000 1000
	final static byte controlled_study_mask = 4; // binary 0000 0100
	final static byte govt_funded_mask = 2; // binary 0000 0010
	final static byte fda_approved_mask = 1; // binary 0000 0001

	public static void main(String[] args) {

		int comparatorvalue = 0;
		RandomAccessFile raf = null;
		Scanner scanner = new Scanner(System.in);
		BufferedReader br_read = new BufferedReader(new InputStreamReader(
				System.in));

		try {

			while (true) {
				int option = show_menu(scanner);

				if (option == 1) {
					parse_file();
					System.out.println("\nParsing Done...!!\n");
				}

				try{
				raf = new RandomAccessFile(new File(DATA_FILE_NAME), "r");
				}
				catch (FileNotFoundException e) // main try catch
				{
					System.out.println(e.getMessage());
					System.out.println("");
					continue;
				}
				
				int fieldvalue = show_qury_menu(scanner);

				System.out.print("Enter the value to be searched : ");
				String searchvalue = br_read.readLine();

				if (fieldvalue != 2 && fieldvalue != 3 && fieldvalue < 8) {
					System.out.println("Press 1 for == \n" + "Press 2 for > \n"
							+ "Press 3 for < \n" + "Press 4 for <=\n"
							+ "Press 5 for >= \n");
					System.out.print("Enter the comparator value : ");
					comparatorvalue = scanner.nextInt();
				}

				try {
					switch (fieldvalue) {
					case 1:
						search_num("id.ndx", searchvalue, comparatorvalue, raf);
						break;
					case 2:
						search_text("company.ndx", searchvalue, raf);
						break;
					case 3:
						search_text("drug.ndx", searchvalue, raf);
						break;
					case 4:
						search_num("trails.ndx", searchvalue, comparatorvalue,
								raf);
						break;
					case 5:
						search_num("patients.ndx", searchvalue,
								comparatorvalue, raf);
						break;

					case 6:
						search_num("dosage.ndx", searchvalue, comparatorvalue,
								raf);
						break;
					case 7:
						search_num("reading.ndx", searchvalue, comparatorvalue,
								raf);
						break;
					case 8:
						search_text("double_blind.ndx", searchvalue, raf);
						break;
					case 9:
						search_text("controlled_study.ndx", searchvalue, raf);
						break;
					case 10:
						search_text("govt_funded.ndx", searchvalue, raf);
						break;
					case 11:
						search_text("fda_approved.ndx", searchvalue, raf);
						break;
					}
				} catch (Exception e) // switch try catch
				{

				}
			}
		} 
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			
		}
		finally
		{
			scanner.close();
		}
	}

	public static void read(long seekvalue, RandomAccessFile raf)
			throws IOException {

		byte double_blind_mask = 8; // binary 0000 1000
		byte controlled_study_mask = 4; // binary 0000 0100
		byte govt_funded_mask = 2; // binary 0000 0010
		byte fda_approved_mask = 1; // binary 0000 0001

		String output = "";

		raf.seek(seekvalue);

		output = output + raf.readInt() + ",";
		// System.out.println(raf.readInt());
		int length = (int) raf.read();
		char[] bytes = new char[length];
		for (int j = 0; j < length; j++) {
			bytes[j] = (char) raf.readByte();
		}

		output = output + new String(bytes) + ",";
		// System.out.println(new String(bytes));
		char[] drug = new char[6];
		for (int k = 0; k < 6; k++) {
			drug[k] = (char) raf.readByte();
		}
		output = output + new String(drug) + ",";
		// System.out.println(new String(drug));
		output = output + raf.readShort() + ",";
		// System.out.println(raf.readShort());
		output = output + raf.readShort() + ",";
		// System.out.println(raf.readShort());
		output = output + raf.readShort() + ",";
		// System.out.println(raf.readShort());
		output = output + raf.readShort() + ",";
		// System.out.println(raf.readFloat());
		byte statbool = (byte) raf.read();

		for (int l = 0; l < 4; l++) {
			switch (l) {
			case 0:
				byte double_blind_mask1 = (byte) (statbool & double_blind_mask);
				if (double_blind_mask1 == double_blind_mask)
					output = output + "True" + ",";
				// System.out.println("true");
				else
					output = output + "False" + ",";
				// System.out.println("false");
				break;

			case 1:

				byte controlled_study_mask1 = (byte) (statbool & controlled_study_mask);
				if (controlled_study_mask1 == controlled_study_mask)
					output = output + "True" + ",";
				// System.out.println("true");
				else
					output = output + "False" + ",";
				// System.out.println("false");

				break;

			case 2:

				byte govt_funded_mask1 = (byte) (statbool & govt_funded_mask);
				if (govt_funded_mask1 == govt_funded_mask)
					output = output + "True" + ",";
				// System.out.println("true");
				else
					output = output + "False" + ",";
				// System.out.println("false");

				break;

			case 3:

				byte fda_approved_mask1 = (byte) (statbool & fda_approved_mask);
				if (fda_approved_mask1 == fda_approved_mask)
					output = output + "True" + ",";
				// System.out.println("true");
				else
					output = output + "False" + ",";
				// System.out.println("false");

				break;
			}
		}
		
		System.out.println(output);

	}

	public static void search_num(String file_name, String searchvalue,
			int comparatorvalue, RandomAccessFile raf) throws Exception {
		FileInputStream fissearch = null;
		BufferedReader brsearch = null;
		String linesearch = null;

		fissearch = new FileInputStream(file_name);
		brsearch = new BufferedReader(new InputStreamReader(fissearch));

		int count = 0;
		
		String[] result = null;
		Long search_1 = Long.parseLong(searchvalue.trim());
		while ((linesearch = brsearch.readLine()) != null) {

			result = linesearch.split("->");
			Long file_1 = Long.parseLong(result[0].trim());

			if (comparatorvalue == 1) {
				if (file_1 != search_1)
					continue;

			} else if (comparatorvalue == 2) {
				if (file_1 <= search_1)
					continue;

			} else if (comparatorvalue == 3) {
				if (file_1 >= search_1)
					continue;
			} else if (comparatorvalue == 4) {
				if (file_1 > search_1)
					continue;
			} else if (comparatorvalue == 5) {
				if (file_1 < search_1)
					continue;
			}
			count ++;
			StringTokenizer stsearch = new StringTokenizer(result[1], ",");
			while (stsearch.hasMoreTokens()) {
				Long seekvalue = Long.parseLong(stsearch.nextToken().trim());
				read(seekvalue, raf);
			}
			if (comparatorvalue == 1)
				break;

		}
		
		if (count == 0)
			System.out.println("\nNo records found for : " + searchvalue);
		brsearch.close();
	}

	public static void search_text(String file_name, String searchvalue,
			RandomAccessFile raf) throws Exception {
		FileInputStream fissearch = null;
		BufferedReader brsearch = null;
		String linesearch = null;

		fissearch = new FileInputStream(file_name);
		brsearch = new BufferedReader(new InputStreamReader(fissearch));

		int count = 0;
		
		while ((linesearch = brsearch.readLine()) != null) {
			String asdf = searchvalue + " ->";
			if (linesearch.contains(asdf)) {
				count ++;
				String[] result = linesearch.split("->");
				StringTokenizer stsearch = new StringTokenizer(result[1], ",");
				while (stsearch.hasMoreTokens()) {
					Long seekvalue = Long
							.parseLong(stsearch.nextToken().trim());
					read(seekvalue, raf);
				}

				break;
			}

		}
		if (count == 0)
			System.out.println("\nNo records found for : " + searchvalue);
		brsearch.close();
	}

	public static void parse_file() {
		RandomAccessFile raf = null;
		FileReader fis = null;
		FileWriter fstream = null;
		BufferedWriter out = null;
		File f = new File(CSV_FILE_NAME);
		HashMap<Integer, Long> idmap = new HashMap<>();
		HashMap<String, String> companymap = new HashMap<>();
		HashMap<String, String> drugmap = new HashMap<>();
		HashMap<Integer, String> trailmap = new HashMap<>();
		HashMap<Integer, String> patientsmap = new HashMap<>();
		HashMap<Integer, String> dosagemap = new HashMap<>();
		HashMap<Float, String> readingmap = new HashMap<>();
		HashMap<String, String> doubleblindmap = new HashMap<>();
		HashMap<String, String> controlledstudymap = new HashMap<>();
		HashMap<String, String> govtfundmap = new HashMap<>();
		HashMap<String, String> fdaapprovedmap = new HashMap<>();

		try {

			raf = new RandomAccessFile(new File(DATA_FILE_NAME), "rw");
			fis = new FileReader(f);

			CSVReader reader = new CSVReader(fis);
			String[] header = reader.readNext();
			String[] line = reader.readNext();

			while (line != null) {
				int id = Integer.parseInt(line[0]);
				String companyname = line[1];
				String drugid = line[2];
				int trails = Integer.parseInt(line[3]);
				int patients = Integer.parseInt(line[4]);
				int dossage = Integer.parseInt(line[5]);
				Float reading = Float.parseFloat(line[6]);
				String doubleblind = line[7];
				String controlledstudy = line[8];
				String govtfunded = line[9];
				String fdaapproved = line[10];

				byte boolvalues = 0;
				for (int i = 7; i < 11; i++) {

					switch (i) {

					case 7:
						if (line[i].equals("true")) {
							boolvalues = (byte) (boolvalues | double_blind_mask);
						}

						break;

					case 8:
						if (line[i].equals("true")) {
							boolvalues = (byte) (boolvalues | controlled_study_mask);
						}

						break;

					case 9:
						if (line[i].equals("true")) {
							boolvalues = (byte) (boolvalues | govt_funded_mask);
						}

						break;

					case 10:
						if (line[i].equals("true")) {
							boolvalues = (byte) (boolvalues | fda_approved_mask);
						}

						break;
					}
				}

				Long idloc = raf.getFilePointer();
				idmap.put(id, idloc);
				raf.writeInt(id);

				raf.write(companyname.length());

				String temp = "";

				if (companymap.containsKey(companyname)) {
					temp = companymap.get(companyname);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";

				companymap.put(companyname, temp);
				raf.writeBytes(companyname);

				temp = "";

				if (drugmap.containsKey(drugid)) {
					temp = drugmap.get(drugid);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";

				drugmap.put(drugid, temp);
				raf.writeBytes(drugid);

				temp = "";

				if (trailmap.containsKey(trails)) {
					temp = trailmap.get(trails);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				trailmap.put(trails, temp);
				raf.writeShort(trails);

				temp = "";

				if (patientsmap.containsKey(patients)) {
					temp = patientsmap.get(patients);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				patientsmap.put(patients, temp);
				raf.writeShort(patients);

				temp = "";

				if (dosagemap.containsKey(dossage)) {
					temp = dosagemap.get(dossage);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				dosagemap.put(dossage, temp);
				raf.writeShort(dossage);

				temp = "";

				if (readingmap.containsKey(reading)) {
					temp = readingmap.get(reading);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				readingmap.put(reading, temp);
				raf.writeFloat(reading);

				temp = "";

				if (doubleblindmap.containsKey(doubleblind)) {
					temp = dosagemap.get(doubleblind);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				doubleblindmap.put(doubleblind, temp);

				temp = "";

				if (controlledstudymap.containsKey(controlledstudy)) {
					temp = controlledstudymap.get(controlledstudy);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				controlledstudymap.put(controlledstudy, temp);

				temp = "";

				if (govtfundmap.containsKey(govtfunded)) {
					temp = govtfundmap.get(govtfunded);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				govtfundmap.put(govtfunded, temp);

				temp = "";

				if (fdaapprovedmap.containsKey(fdaapproved)) {
					temp = fdaapprovedmap.get(fdaapproved);
					temp = temp + String.valueOf(idloc) + ",";
				} else
					temp = String.valueOf(idloc) + ",";
				fdaapprovedmap.put(fdaapproved, temp);

				raf.write(boolvalues);

				line = reader.readNext();
			}

			fstream = new FileWriter("id.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<Integer, Long> entry : idmap.entrySet()) {
				Integer key = entry.getKey();
				Long value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();

			}
			out.close();

			fstream = new FileWriter("company.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<String, String> entry : companymap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}
			out.close();

			fstream = new FileWriter("drug.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<String, String> entry : drugmap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("trails.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<Integer, String> entry : trailmap.entrySet()) {
				Integer key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("patients.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<Integer, String> entry : patientsmap.entrySet()) {
				Integer key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("dosage.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<Integer, String> entry : dosagemap.entrySet()) {
				Integer key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("reading.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<Float, String> entry : readingmap.entrySet()) {
				Float key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("double_blind.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<String, String> entry : doubleblindmap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("controlled_stydy.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<String, String> entry : controlledstudymap
					.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("govt_funded.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<String, String> entry : govtfundmap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}

			out.close();

			fstream = new FileWriter("fda_approved.ndx", false);
			out = new BufferedWriter(fstream);

			for (Map.Entry<String, String> entry : fdaapprovedmap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				out.append(key.toString() + " -> " + value.toString());
				out.flush();
				out.newLine();
			}
		} catch (IOException e) {

		} finally {
			try {
				raf.close();
			} catch (IOException e) {
				System.out.println("error message");
			}
			try {
				fis.close();
			} catch (IOException e) {
				System.out.println("error message");
			}

		}

	}

	public static int show_menu(Scanner scanner) {
		int out = 0;
		while (true) {
			System.out.println("\n");
			System.out
					.println("***********************************************");
			System.out
					.println("**************** Main Menu ********************");
			System.out
					.println("***********************************************");
			System.out.println("1 - Parse the CSV File");
			System.out.println("2 - Display Query Menu");
			System.out.println("3 - Exit");

			System.out.print("\nEnter option : ");
			out = scanner.nextInt();

			if (out == 3)
			{
				System.out.println("\nBye Bye....!!!");
				System.exit(0);
			}
			break;
		}

		return out;

	}

	public static int show_qury_menu(Scanner scanner) {
		int fieldvalue = 0;
		while (true) {
			System.out.println("\n");
			System.out
					.println("************************************************");
			System.out
					.println("**************** Query Menu ********************");
			System.out
					.println("************************************************");
			System.out.println("Press 1 if u want to query on Id \n"
					+ "Press 2 if u want to query on Company \n"
					+ "Press 3 if u want to query on Drugid \n"
					+ "Press 4 if u want to query on Trails \n"
					+ "Press 5 if u want to query on Patients \n"
					+ "Press 6 if u want to query on Dossage \n"
					+ "Press 7 if u want to query on Reading \n"
					+ "Press 8 if u want to query on Double_blind \n"
					+ "Press 9 if u want to query on Controlled_study \n"
					+ "Press 10 if u want to query on govt_fund \n"
					+ "Press 11 if u want to query on fda_approved \n");

			System.out.print("Enter option : ");
			fieldvalue = scanner.nextInt();

			if (fieldvalue < 1 && fieldvalue > 11)
				continue;

			break;
		}

		return fieldvalue;

	}

} // end Class
