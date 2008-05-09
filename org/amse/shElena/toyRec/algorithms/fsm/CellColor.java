package org.amse.shElena.toyRec.algorithms.fsm;

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
	};

	public Color getColor() {
		return null;
	};
}
