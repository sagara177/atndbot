package atndbot.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AtndCalendar {

	public static List<String> getYYYYMMList(int months) {
		List<String> yyyymmList = new ArrayList<String>();
		
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		
		for (int i = 0; i < months; i++) {
			// Calendar.MONTH starts 0, ends 11
			if (month < 12) {
				String y = String.format("%04d", year);
				String m = String.format("%02d", month + 1);
				yyyymmList.add(y + m);
			}
			else {
				// 201013 == 201101
				String y = String.format("%04d", year + 1);
				String m = String.format("%02d", month - 12 + 1);
				yyyymmList.add(y + m);
			}
			month++;
		}
		
		return yyyymmList;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> yyyymmList = AtndCalendar.getYYYYMMList(3);
		for (String yyyymm : yyyymmList) {
			System.out.println(yyyymm);
		}
	}

}
