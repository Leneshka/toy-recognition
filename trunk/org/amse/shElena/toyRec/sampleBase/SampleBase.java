package org.amse.shElena.toyRec.sampleBase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.amse.shElena.toyRec.samples.ISample;
import org.amse.shElena.toyRec.samples.Sampler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SampleBase implements ISampleBase {

	private List<ISample> mySamples;

	private final int mySampleSize;

	//private int myStateID;

	public SampleBase(int size) {
		mySampleSize = size;
		mySamples = new ArrayList<ISample>();
	}

	public void addSample(ISample sample) {
		mySamples.add(sample);
		//baseChanged();
	}
	
	public void addSampleBase(ISampleBase base) {
		if(mySampleSize != base.getSampleSize()){
			return;
		}
		
		for(ISample sample : base.getSamples()){
			mySamples.add(sample);
		}
	}

	public void removeSample(ISample sample) {
		mySamples.remove(sample);
		//baseChanged();
	}

	public boolean isEmpty() {
		return mySamples.isEmpty();
	}

	public void saveSampleBase(File file) {
		DocumentBuilder builder = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document document = builder.newDocument();

		Element root = document.createElement("DataBase");
		root.setAttribute("sampleSize", String.valueOf(mySampleSize));

		for (ISample s : mySamples) {
			Element e = document.createElement("Character");
			root.appendChild(e);
			e.setAttribute("symbol", s.getSymbol().toString());

			e.setAttribute("picture", s.writePictureToString());
		}

		document.appendChild(root);

		try {
			Source source = new DOMSource(document);
			Result result = new StreamResult(file);

			TransformerFactory tf = TransformerFactory.newInstance();
			tf.setAttribute("indent-number", new Integer(2));

			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Symbol base wasn`t saved.");
		} catch (TransformerException e) {
			throw new RuntimeException("Symbol base wasn`t saved.");
		}

	}

	public void loadSampleBase(File file) {
		Document document = null;

		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			document = builder.parse(file);

			if (document != null) {
				Node root = document.getDocumentElement();

				String sampleSize = root.getAttributes().getNamedItem(
						"sampleSize").getNodeValue();

				if (Integer.parseInt(sampleSize) != mySampleSize) {
					throw new WrongSizeException();
				}

				NodeList children = root.getChildNodes();
				final int childNumber = children.getLength();

				for (int i = 0; i < childNumber; ++i) {
					Node node = children.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {

						String symbol = node.getAttributes().getNamedItem(
								"symbol").getNodeValue();
						String picture = node.getAttributes().getNamedItem(
								"picture").getNodeValue();

						ISample s = Sampler.parseSample(symbol, picture);

						mySamples.add(s);
					}
				}
			}
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Symbol base wasn`t loaded.");
		} catch (SAXException e) {
			throw new RuntimeException("Symbol base wasn`t loaded.");
		} catch (IOException e) {
			throw new RuntimeException("Symbol base wasn`t loaded.");
		} catch (WrongSizeException e) {
			throw new RuntimeException(
					"Symbol base wasn`t loaded: wrong size of samples.");
		} catch (RuntimeException e) {
			throw new RuntimeException(
					"Symbol base wasn`t loaded: file contains forbidden symbol.");
		}
		//baseChanged();

	}

	public class WrongSizeException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

	/*public int getStateID() {
		return myStateID;
	}

	private void baseChanged() {
		myStateID++;
	}*/

	public int size() {
		return mySamples.size();
	}

	/*
	 * obernut` udalenie
	 */
	public Iterator<ISample> iterator() {
		return mySamples.iterator();
	}

	public ISample[] getSamples() {
		ISample[] res = new ISample[mySamples.size()];
		return mySamples.toArray(res);
	}

	public int getSampleSize() {
		return mySampleSize;
	}

	public void clear() {
		mySamples.clear();
	}

}
