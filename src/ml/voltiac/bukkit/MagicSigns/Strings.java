package ml.voltiac.bukkit.MagicSigns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface Strings {
	/**
	 * Intarnal use only!
	 * 
	 */
	public class permissions {

		private String k = null;
		
		private permissions(String key) {

		}

		public String SIGN_USE = "MagicSigns.use." + k;
		public String SIGN_CREATE = "MagicSigns.use." + k;
		public String SIGN_BREAK = "MagicSigns.use." + k;

		
	}
	
	public class get {
		public static permissions permissionKey(String key) {
			return new permissions(key);
		}
	}
}
