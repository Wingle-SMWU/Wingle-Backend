package kr.co.wingle.common.filter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ReadableResponseBodyWrapper extends HttpServletResponseWrapper {

	public class ResponseBodyServletOutputStream extends ServletOutputStream {

		private final DataOutputStream outputStream;

		public ResponseBodyServletOutputStream(OutputStream output) {
			this.outputStream = new DataOutputStream(output);
		}

		@Override
		public void write(int b) throws IOException {
			outputStream.write(b);
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener listener) {
		}
	}

	private ByteArrayOutputStream byteArrayOutputStream;

	public ReadableResponseBodyWrapper(HttpServletResponse response) throws IOException {
		super(response);

		byteArrayOutputStream = new ByteArrayOutputStream();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		ResponseBodyServletOutputStream responseBodyServletOutputStream = new ResponseBodyServletOutputStream(
			byteArrayOutputStream);

		return responseBodyServletOutputStream;
	}

	public byte[] getDataStream() {
		return byteArrayOutputStream.toByteArray();
	}

	public String getResponseBody() {
		return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
	}
}