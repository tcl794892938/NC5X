package nc.vo.pubtools;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nc.vo.pub.BusinessException;

/**
 * IO�ļ����������
 * 
 * @author sun_jian
 * @version 1.0.0
 */
public class IOUtils {

	/**
	 * �����
	 */
	private OutputStream os = null;

	/**
	 * ������
	 */
	private InputStream is = null;

	/**
	 * �ֽ�����
	 */
	private byte writeBuffer[] = null;

	/**
	 * ���캯��
	 * 
	 * @param java.lang.String
	 *            fileName
	 * @param boolean
	 *            in
	 * @param boolean
	 *            out
	 * @throws java.lang.Exception
	 */
	public IOUtils(String fileName, boolean in, boolean out) throws Exception {

		this(new File(fileName), in, out);

	}

	/**
	 * ���캯��
	 * 
	 * @param java.io.File
	 *            file
	 * @param boolean
	 *            in
	 * @param boolean
	 *            out
	 * @throws java.lang.Exception
	 */
	public IOUtils(File file, boolean in, boolean out) throws Exception {

		if (null == file)

			throw new BusinessException("Invalid fileName");

		if (in) {

			this.is = new FileInputStream(file);

		}

		if (out) {

			this.os = new FileOutputStream(file);

		}

	}

	/**
	 * ���int
	 * 
	 * @param int
	 *            i
	 * @throws java.io.IOException
	 */
	public final void write(int i) throws IOException {

		this.os.write(i);

	}

	/**
	 * ���byte[]
	 * 
	 * @param byte[]
	 *            i
	 * @throws java.io.IOException
	 */
	public final void write(byte[] i) throws IOException {

		this.os.write(i);

	}

	/**
	 * ���char
	 * 
	 * @param char
	 *            i
	 * @throws java.io.IOException
	 */
	public final void write(char i) throws IOException {

		this.os.write(Character.toString(i).getBytes());

	}

	/**
	 * ���String
	 * 
	 * @param java.lang.String
	 *            i
	 * @throws java.io.IOException
	 */
	public final void write(String i) throws IOException {

		this.os.write(i.getBytes());

	}

	/**
	 * ���boolean
	 * 
	 * @param boolean
	 *            i
	 * @throws java.io.IOException
	 */
	public final void write(boolean i) throws IOException {

		this.os.write(i ? 1 : 0);

	}

	/**
	 * ���long
	 * 
	 * @param long
	 *            i
	 * @throws java.io.IOException
	 */
	public final void write(long i) throws IOException {

		this.writeBuffer = new byte[8];

		writeBuffer[0] = (byte) (i >>> 56);

		writeBuffer[1] = (byte) (i >>> 48);

		writeBuffer[2] = (byte) (i >>> 40);

		writeBuffer[3] = (byte) (i >>> 32);

		writeBuffer[4] = (byte) (i >>> 24);

		writeBuffer[5] = (byte) (i >>> 16);

		writeBuffer[6] = (byte) (i >>> 8);

		writeBuffer[7] = (byte) (i >>> 0);

		this.os.write(writeBuffer, 0, 8);

	}

	/**
	 * ���float
	 * 
	 * @param float
	 *            i
	 * @throws java.io.IOException
	 */
	public final void writeFloat(float v) throws IOException {

		this.write(Float.floatToIntBits(v));

	}

	/**
	 * ���double
	 * 
	 * @param double
	 *            i
	 * @throws java.io.IOException
	 */
	public final void writeDouble(double v) throws IOException {

		this.write(Double.doubleToLongBits(v));

	}

	/**
	 * �����ˢ�»�����
	 * 
	 * @throws IOException
	 */
	public final void flush() throws IOException {

		this.os.flush();

	}

	/**
	 * ����������ֵ
	 * 
	 * @return java.lang.Integer
	 * @throws java.lang.IOException
	 */
	public final int readInt() throws IOException {

		int ch1 = this.is.read();

		int ch2 = this.is.read();

		int ch3 = this.is.read();

		int ch4 = this.is.read();

		if ((ch1 | ch2 | ch3 | ch4) < 0)

			throw new EOFException();

		return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));

	}

	/**
	 * ���������ֽ�
	 * 
	 * @param java.lang.Integer
	 *            i
	 * @throws java.lang.IOException
	 */
	public void skip(long i) throws IOException {

		this.is.skip(i);

	}

	/**
	 * �����ֽ����飬����Ѿ������һ���ֽڣ��շ���-1
	 * 
	 * @param byte[]
	 *            i
	 * @return java.lang.Integer
	 * @throws java.lang.IOException
	 */
	public final int read(byte[] i) throws IOException {

		return this.is.read(i);

	}

	/**
	 * ����booleanֵ
	 * 
	 * @return java.lang.Boolean
	 * @throws java.lang.IOException
	 */
	public final boolean readBoolean() throws IOException {

		int ch = this.is.read();

		if (ch < 0)

			throw new EOFException();

		return (ch != 0);

	}

	/**
	 * �����ֽ�
	 * 
	 * @return java.lang.Byte
	 * @throws java.lang.IOException
	 */
	public final byte readByte() throws IOException {

		int ch = this.is.read();

		if (ch < 0)

			throw new EOFException();

		return (byte) (ch);

	}

	/**
	 * ����short
	 * 
	 * @return java.lang.Short
	 * @throws java.lang.IOException
	 */
	public final short readShort() throws IOException {

		int ch1 = this.is.read();

		int ch2 = this.is.read();

		if ((ch1 | ch2) < 0)

			throw new EOFException();

		return (short) ((ch1 << 8) + (ch2 << 0));

	}

	/**
	 * ����char
	 * 
	 * @return java.lang.Character
	 * @throws java.lang.IOException
	 */
	public final char readChar() throws IOException {

		int ch1 = this.is.read();

		int ch2 = this.is.read();

		if ((ch1 | ch2) < 0)

			throw new EOFException();

		return (char) ((ch1 << 8) + (ch2 << 0));

	}

	/**
	 * ����long
	 * 
	 * @return java.lang.Long
	 * @throws java.lang.IOException
	 */
	public final long readLong() throws IOException {

		this.writeBuffer = new byte[8];

		readFully(writeBuffer, 0, 8);

		return (((long) writeBuffer[0] << 56)

		+ ((long) (writeBuffer[1] & 255) << 48)

		+ ((long) (writeBuffer[2] & 255) << 40)

		+ ((long) (writeBuffer[3] & 255) << 32)

		+ ((long) (writeBuffer[4] & 255) << 24)

		+ ((writeBuffer[5] & 255) << 16) + ((writeBuffer[6] & 255) << 8) + ((writeBuffer[7] & 255) << 0));

	}

	/**
	 * ����float
	 * 
	 * @return java.lang.Float
	 * @throws java.lang.IOException
	 */
	public final float readFloat() throws IOException {

		return Float.intBitsToFloat(readInt());

	}

	/**
	 * ����doutble
	 * 
	 * @return java.lang.Double
	 * @throws java.lang.IOException
	 */
	public final double readDouble() throws IOException {

		return Double.longBitsToDouble(readLong());

	}

	/**
	 * �ر���
	 * 
	 * @throws IOException
	 */
	public final void closeStream() throws IOException {

		if (null != this.is) {

			this.is.close();

		}

		if (null != this.os) {

			this.os.close();

		}

	}

	/**
	 * �Ӱ������������ж�ȡ�˲�����Ҫ���ֽڡ�
	 * 
	 * @param java.lang.Byte
	 *            b �洢��ȡ���ݵĻ�����
	 * @param java.lang.Integer
	 *            off ���ݵ���ʼƫ����
	 * @param java.lang.Integer
	 *            len Ҫ��ȡ���ֽ���
	 * @throws java.lang.IOException
	 *             ���ѹرղ��Ұ������������ڹرպ�֧�ֶ�ȡ���������߷������� I/O ���� ����μ���
	 * 
	 */
	private final void readFully(byte b[], int off, int len) throws IOException {

		if (len < 0)

			throw new IndexOutOfBoundsException();

		int n = 0;

		while (n < len) {

			int count = this.is.read(b, off + n, len - n);

			if (count < 0)

				throw new EOFException();

			n += count;

		}

	}

	/**
	 * ��ȡ������
	 * 
	 * @return java.io.InputStream
	 */
	public final InputStream getInputStream() {

		return is;

	}

	/**
	 * ��ȡ�����
	 * 
	 * @return java.io.OutputStream
	 */
	public final OutputStream getOutputStream() {

		return os;

	}

}
