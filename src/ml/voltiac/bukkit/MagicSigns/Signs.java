package ml.voltiac.bukkit.MagicSigns;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;

public interface Signs {
	public class SignText {
		public String line0 = null;
		public String line1 = null;
		public String line2 = null;
		public String line3 = null;

		SignText(String l0, String l1, String l2, String l3) {
			line0 = l0;
			line1 = l1;
			line2 = l2;
			line3 = l3;
		}
		
		public String[] toArray(){
			String[] str = {line0, line1, line2, line3};
			return str;
			
		}
		
		public static SignText toSignText(List<String> list) {
			String[] lines = { "Error", "Error", "Error", "Error" };
			int i = 0;

			for (String s : list) {
				lines[i] = s;
				i++;
			}
			SignText st = new SignText(lines[0], lines[1], lines[2], lines[3]);
			return st;
		}

		public static SignText toSignText(String[] array) {
			SignText st = new SignText(array[0], array[1], array[2], array[3]);
			return st;
		}

		public static SignText toSignText(ItemStack itemStack) {
			Sign sign = (Sign) itemStack.getData();
			return toSignText(sign.getLines());
		}

		public static SignText toSignText(Block block) {
			Sign sign = (Sign) block;
			return toSignText(sign.getLines());
		}
		public static SignText toSignText(Sign sign) {
			return toSignText(sign.getLines());
		}

		

	}
}
