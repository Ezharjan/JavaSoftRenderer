/**
 * by Alexander Ezharjan
 * 22th,June,2021
 * contact via email: mysoft@111.com
 */

import java.io.IOException;

/**
 * Represents a 3D Star field that can be rendered into an image.
 */
public class Stars3D
{
	/** How much the stars are spread out in 3D space, on average. */
	private final float m_spread;
	/** How quickly the stars move towards the camera */
	private final float m_speed;

	/** The star positions on the X axis */
	private final float m_starX[];
	/** The star positions on the Y axis */
	private final float m_starY[];
	/** The star positions on the Z axis */
	private final float m_starZ[];

	/**
	 * Creates a new 3D star field in a usable state.
	 *
	 * @param numStars The number of stars in the star field
	 * @param spread   How much the stars spread out, on average.
	 * @param speed    How quickly the stars move towards the camera
	 */
	public Stars3D(int numStars, float spread, float speed) throws IOException
	{
		m_spread = spread;
		m_speed = speed;

		m_starX = new float[numStars];
		m_starY = new float[numStars];
		m_starZ = new float[numStars];

		for(int i = 0; i < m_starX.length; i++)
		{
			InitStar(i);
		}

		m_bitmap = new Bitmap("./res/bricks.jpg");
	}

	private final Bitmap m_bitmap;

	/**
	 * Initializes a star to a new pseudo-random location in 3D space.
	 *
	 * @param i The index of the star to initialize.
	 */
	private void InitStar(int i)
	{
		//The random values have 0.5 subtracted from them and are multiplied
		//by 2 to remap them from the range (0, 1) to (-1, 1).
		m_starX[i] = 2 * ((float)Math.random() - 0.5f) * m_spread;
		m_starY[i] = 2 * ((float)Math.random() - 0.5f) * m_spread;
		//For Z, the random value is only adjusted by a small amount to stop
		//a star from being generated at 0 on Z.
		m_starZ[i] = ((float)Math.random() + 0.00001f) * m_spread;
	}

	/**
	 * Updates every star to a new position, and draws the starfield in a
	 * bitmap.
	 *
	 * @param target The bitmap to render to.
	 * @param delta  How much time has passed since the last update.
	 */
	public void UpdateAndRender(RenderContext target, float delta)
	{
		final float tanHalfFOV = (float)Math.tan(Math.toRadians(90.0/2.0));
		//Stars are drawn on a black background
		target.Clear((byte)0x00);

		float halfWidth  = target.GetWidth()/2.0f;
		float halfHeight = target.GetHeight()/2.0f;
		int triangleBuilderCounter = 0;

		int x1 = 0;
		int y1 = 0;
		int x2 = 0;
		int y2 = 0;
		for(int i = 0; i < m_starX.length; i++)
		{
			//Update the Star.

			//Move the star towards the camera which is at 0 on Z.
			m_starZ[i] -= delta * m_speed;

			//If star is at or behind the camera, generate a new position for
			//it.
			if(m_starZ[i] <= 0)
			{
				InitStar(i);
			}

			//Render the Star.

			//Multiplying the position by (size/2) and then adding (size/2)
			//remaps the positions from range (-1, 1) to (0, size)

			//Division by z*tanHalfFOV moves things in to create a perspective effect.
			int x = (int)((m_starX[i]/(m_starZ[i] * tanHalfFOV)) * halfWidth + halfWidth);
			int y = (int)((m_starY[i]/(m_starZ[i] * tanHalfFOV)) * halfHeight + halfHeight);
//
//			int x = (int)((m_starX[i]) * halfWidth + halfWidth);
//			int y = (int)((m_starY[i]) * halfHeight + halfHeight);


			//If the star is not within range of the screen, then generate a
			//new position for it.
			if(x < 0 || x >= target.GetWidth() ||
				(y < 0 || y >= target.GetHeight()))
			{
				InitStar(i);
				continue;
			}
//			else
//			{
//				//Otherwise, it is safe to draw this star to the screen.
//				target.DrawPixel(x, y, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
//			}
			triangleBuilderCounter++;
			if(triangleBuilderCounter == 1)
			{
				x1 = x;
				y1 = y;
			}
			else if(triangleBuilderCounter == 2)
			{
				x2 = x;
				y2 = y;
			}
			else if(triangleBuilderCounter == 3)
			{
				triangleBuilderCounter = 0;
//				Vertex v1 = new Vertex(new Vector4f(x1/400.0f - 1.0f, y1/300.0f - 1.0f, 0.0f, 1.0f), 
//						new Vector4f(1.0f, 0.0f, 0.0f, 0.0f));
//				Vertex v2 = new Vertex(new Vector4f(x2/400.0f - 1.0f, y2/300.0f - 1.0f, 0.0f, 1.0f), 
//						new Vector4f(1.0f, 1.0f, 0.0f, 0.0f));
//				Vertex v3 = new Vertex(new Vector4f(x/400.0f - 1.0f, y/300.0f - 1.0f, 0.0f, 1.0f), 
//						new Vector4f(0.0f, 1.0f, 0.0f, 0.0f));
//
//				target.DrawTriangle(v1, v2, v3, m_bitmap);
			}
		}
	}
}
