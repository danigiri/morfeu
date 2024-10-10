// SVG VIEW MODULE . JAVA

package cat.calidos.morfeu.view.injection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.RoundRectangle2D;
import java.io.StringWriter;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Provider;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.w3c.dom.DOMImplementation;

import dagger.Module;
import dagger.Provides;


/**
 * @author daniel giribet
 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
@Module
public class SVGViewModule {

private static final int	ARC						= 10;
private static final int	TRUNCATE_TEXT_LENGHT	= 12;
private static final int	FONT_SIZE				= 18;
private static final int	BORDER_SIZE				= 2;
private static final int	RECTANGLE_WIDTH			= 88;
private static final int	RECTANGLE_HEIGHT		= 70;
private static final int	TEXT_START				= BORDER_SIZE + 2;
private static final int	TEXT_MAX_WIDTH			= 80;
private static final int	TEXT_MAX_HEIGHT			= 80;

@Provides
public static String render(@Named("CompletedGraphics") SVGGraphics2D generator,
							StringWriter writer) {

	boolean useCSS = true;
	try {
		generator.stream(writer, useCSS);
	} catch (SVGGraphics2DIOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return writer.toString();

}


@Provides
public static DOMImplementation domImplementation() {
	return GenericDOMImplementation.getDOMImplementation();
}


@Provides
public static org.w3c.dom.Document document(DOMImplementation domImplementation) {
	return domImplementation.createDocument("http://www.w3.org/2000/svg", "svg", null);
}


@Provides
public static SVGGraphics2D generator(	org.w3c.dom.Document document,
										Font font) {

	SVGGraphics2D generator = new SVGGraphics2D(document);
	generator.setSVGCanvasSize(new Dimension(RECTANGLE_WIDTH, RECTANGLE_HEIGHT)); // set total
																					// viewport to
																					// the minimum

	generator.setPaint(Color.DARK_GRAY);
	RoundRectangle2D.Double rect = new RoundRectangle2D.Double(0, 0, RECTANGLE_WIDTH,
			RECTANGLE_HEIGHT, ARC, ARC);
	generator.fill(rect);

	int innerRectWidth = RECTANGLE_WIDTH - 2 * BORDER_SIZE;
	int innerRectHeight = RECTANGLE_HEIGHT - 2 * BORDER_SIZE;
	rect = new RoundRectangle2D.Double(BORDER_SIZE, BORDER_SIZE, innerRectWidth, innerRectHeight,
			ARC, ARC);
	generator.setPaint(Color.LIGHT_GRAY);
	generator.fill(rect);

	generator.setPaint(Color.BLACK);
	generator.setFont(font);

	return generator;

}


@Provides
public static StringWriter writer() {
	return new StringWriter();
}


@Provides
public static Font font() {
	return new Font("Monospaced", Font.BOLD, FONT_SIZE);
}


@Provides @Named("EffectiveText")
public static String effectiveText(	@Named("Text") String text,
									@Named("Header") Optional<String> header) {
	return header.isPresent() ? "**" + header.get() + "** " + text : text;
}


@Provides
public static AttributedString attributedString(@Named("EffectiveText") String content) {
	return new AttributedString(content);
}


@Provides
public static AttributedCharacterIterator paragraph(AttributedString content) {
	return content.getIterator();
}


@Provides
public static LineBreakMeasurer lineMeasurer(	SVGGraphics2D generator,
												AttributedCharacterIterator paragraph) {
	return new LineBreakMeasurer(paragraph, generator.getFontRenderContext());
}


@Provides @Named("CompletedGraphics")
public static SVGGraphics2D completedGraphics(	boolean truncate,
												@Named("GraphicsShortText") Provider<SVGGraphics2D> providerShortText,
												@Named("GraphicsLongText") Provider<SVGGraphics2D> providerLongText) {

	return truncate ? providerShortText.get() : providerLongText.get();
}


@Provides @Named("GraphicsShortText")
public static SVGGraphics2D graphicsShortText(	@Named("EffectiveText") String content,
												SVGGraphics2D generator) {

	String truncatedContent = content.substring(0,
			Math.min(content.length(), TRUNCATE_TEXT_LENGHT));
	generator.drawString(truncatedContent, TEXT_START, TEXT_START * 3);

	return generator;

}


@Provides @Named("GraphicsLongText")
public static SVGGraphics2D graphicsLongText(	LineBreakMeasurer lineMeasurer,
												AttributedCharacterIterator paragraph,
												SVGGraphics2D generator) {

	float breakWidth = TEXT_MAX_WIDTH;
	float drawPosY = TEXT_START;

	int paragraphStart = paragraph.getBeginIndex();
	lineMeasurer.setPosition(paragraphStart);

	int paragraphEnd = paragraph.getEndIndex();

	while (lineMeasurer.getPosition() < paragraphEnd && drawPosY < TEXT_MAX_HEIGHT) {
		TextLayout layout = lineMeasurer.nextLayout(breakWidth);
		float drawPosX = layout.isLeftToRight() ? TEXT_START : breakWidth - layout.getAdvance();
		drawPosY += layout.getAscent();
		layout.draw(generator, drawPosX, drawPosY);
		drawPosY += layout.getDescent() + layout.getLeading();
	}

	return generator;

}

}

/*
 * Copyright 2018 Daniel Giribet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
