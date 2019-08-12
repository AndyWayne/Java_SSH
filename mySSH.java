import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class mySSH {
	static int sshStatus = 0;
	static Session sshSession = null;
	static Channel openChannel = null;

	public static int startSSH(String sshIP, String sshUser, String sshPwd, int sshPort) {
		sshStatus = 0;
		try {
			JSch myJsch = new JSch();
			sshSession = myJsch.getSession(sshUser, sshIP, sshPort);
			sshSession.setPassword(sshPwd);
			// 设置第一次登陆时候的提示，可选值：(ask|yes|no)
			sshSession.setConfig("StrictHostKeyChecking", "no");
			sshSession.connect(5000);

		} catch (JSchException jSchE) {
			if (jSchE.getMessage().contains("UnknownHostException")) {
				System.out.println("UnknownHostException: " + jSchE.getMessage());
				sshStatus = 1;
			} else if (jSchE.getMessage().contains("timeout")) {
				System.out.println("Connection Timed Out: " + jSchE.getMessage());
				sshStatus = 2;
			} else if (jSchE.getMessage().contains("Auth fail")) {
				System.out.println("Authentication Failed: " + jSchE.getMessage());
				sshStatus = 3;
			} else {
				System.out.println("Other Error: " + jSchE.getMessage());
				sshStatus = 4;
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			sshStatus = 9;
		}
		// System.out.println(sshStatus);
		return sshStatus;
	}

	public static String sendCommands(String sshCmds) {
		String myResult = "";
		try {
			openChannel = sshSession.openChannel("exec");
			((ChannelExec) openChannel).setCommand("su -c '" + sshCmds + "'");

			openChannel.setInputStream(null);
			InputStream sshIns = openChannel.getInputStream();
			OutputStream sshOus = openChannel.getOutputStream();
			openChannel.connect();

			sshOus.write(("cnp200@HW\n").getBytes());
			sshOus.flush();						

			byte[] tmp = new byte[1024];
			while (true) {
				while (sshIns.available() > 0) {
					int i = sshIns.read(tmp, 0, 1024);
					if (i < 0)
						break;
					myResult = new String(tmp, 0 , i);
					System.out.print(new String(tmp, 0, i));
				}
				if (openChannel.isClosed()) {
					if (sshIns.available() > 0)
						continue;
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
		} catch (Exception eee) {
			System.out.println(eee);
		}
		return myResult;

	}

	public static void sshDiscon() {
		try {
			openChannel.disconnect();
			sshSession.disconnect();
		} catch (Exception e) {
			System.out.println("SSH Disconnect ERROR: " + e.getClass());
		}
	}

//	 public static void main(String[] args) {
//	 startSSH("121.12.40.10", "cgpexpert", "mt2013@HW", 22);
//	 sendCommands("iptables -L OUTPUT");
//	 sshDiscon();
//	 }
}
