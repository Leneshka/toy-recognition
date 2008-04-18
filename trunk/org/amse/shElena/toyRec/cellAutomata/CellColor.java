package org.amse.shElena.toyRec.cellAutomata;

import java.awt.Color;

public enum CellColor {
	ORANGE {
		public Color getColor() {
			return Color.ORANGE;
		};
	},

	BLUE {
		public Color getColor() {
			return Color.BLUE;
		};
	},

	GREEN {
		public Color getColor() {
			return Color.GREEN;
		};
	},
	
	
	RED {
		public Color getColor() {
			return Color.RED;
		};
	},

	GRAY {
		public Color getColor() {
			return Color.GRAY;
		};
	}/*,

	BLACK {
		public Color getColor() {
			return Color.BLACK;
		};
	},

	WHITE {
		public Color getColor() {
			return Color.WHITE;
		};
	}*/;

	public Color getColor() {
		return null;
	};
}
