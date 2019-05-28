package com.anvizent.getDataString;

import org.junit.BeforeClass;
import org.junit.Test;

import com.anvizent.client.data.to.csv.path.converter.ClientDBProcessor;
import com.anvizent.client.data.to.csv.path.converter.utilities.SQLUtil;

/**
 * Unit test for simple App.
 */
public class AppTest{
	
	ClientDBProcessor clientDBProcessor = new ClientDBProcessor();
	
	
	@BeforeClass
	public static void beforeClass() {
		
	}
	
	@Test
	public void csvWithRowCount(){
			
			String strReplace = "select min(a) as Incremental_Date "
					+ " from( "
					+ " (select max((to_date('01-01-1988','dd-mm-yyyy') + ser.UDATE/1440)) a from TABULA.ORL$SERIAL ser \n"
					+ "  where ((to_date('01-01-1988','dd-mm-yyyy') + ser.UDATE/1440) >= TO_DATE('2018-01-17 17:16:00.0','yyyy-mm-dd HH24:MI:SS'))) \n"
					+ " union \n"
					+ " (select max((to_date('01-01-1988','dd-mm-yyyy') + al.UDATE/1440)) a from TABULA.ORL$ALINE al \n"
					+ " /* where (to_date('01-01-1988','dd-mm-yyyy') + al.UDATE/1440) >= TO_DATE({date},'yyyy-mm-dd HH24:MI:SS')*/) \n"
					+ " union \n"
					+ " (select max((to_date('01-01-1988','dd-mm-yyyy') + ord.UDATE/1440)) a from TABULA.ORL$ORDERS ord \n"
					+ " /* where (to_date('01-01-1988','dd-mm-yyyy') + ord.UDATE/1440) >= TO_DATE({date},'yyyy-mm-dd HH24:MI:SS')*/) \n"
					+ " union \n"
					+ " (select max((to_date('01-01-1988','dd-mm-yyyy') + ordt.UDATE/1440)) a from TABULA.ORL$ORDERITEMS ordt \n"
					+ " /* where (to_date('01-01-1988','dd-mm-yyyy') + ordt.UDATE/1440) >= TO_DATE({date},'yyyy-mm-dd HH24:MI:SS')*/) \n"
					+ " ) a\n";
			
			try {
				String replacedQuery = SQLUtil.replaceDateValue("2017-02-12 00:00:00",strReplace);
				System.out.println(replacedQuery);
			} catch (Exception e) {
				
			}
			
	}
}
