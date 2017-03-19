package eclipse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Const {

	public static Map<String, String> VOUCH_TYPE = new HashMap<String, String>();
	static {
		VOUCH_TYPE.put("01", "采购入库单");
		VOUCH_TYPE.put("10", "产成品入库单");
		VOUCH_TYPE.put("08", "其他入库单");
		VOUCH_TYPE.put("11", "材料出库单");
		VOUCH_TYPE.put("32", "销售出库单");
		VOUCH_TYPE.put("09", "其他出库单");

	}

	public static List<Select> getVouchTypes() {
		List<Select> rm = new ArrayList<Select>();
		Set<Entry<String, String>> es = VOUCH_TYPE.entrySet();
		for (Entry<String, String> e : es) {
			Select s = new Select();
			s.setText(e.getValue());
			s.setValue(e.getKey());
			rm.add(s);
		}
		return rm;
	}


}
