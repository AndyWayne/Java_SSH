import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Firewall implements ActionListener {
	public static String info = "Welcom to Funny House!";
	JTextField jtfIP = null;
	JTextField jtfUser = new JTextField(10);
	JPasswordField jpdPass = null;
	JLabel jL4 = new JLabel(info, JLabel.CENTER);

	public void go() {
		GridBagLayout myLayout = new GridBagLayout();
		GridBagConstraints myGbc = new GridBagConstraints();

		JFrame myJf = new JFrame("Firewall v1.0");
		myJf.setSize(600, 200);
		myJf.setLocationRelativeTo(null);
		myJf.setLayout(myLayout);

		// Panel 1
		JPanel jP1 = new JPanel();
		jP1.setLayout(myLayout);
		jP1.setBorder(BorderFactory.createTitledBorder("Environment Info"));
		// Label 1
		JLabel jL1 = new JLabel("OMU IP");
		jtfIP = new JTextField(10);
		jtfIP.setPreferredSize(new Dimension(80, 21));
		// Label 2
		JLabel jL2 = new JLabel("User Name");
		jtfUser = new JTextField("cgpexpert");
		jtfUser.setPreferredSize(new Dimension(80, 21));
		// Label 3
		JLabel jL3 = new JLabel("Password");
		jpdPass = new JPasswordField(10);
		jpdPass.setPreferredSize(new Dimension(80, 21));
		// Add components to Panel 1
		jP1.add(jL1);
		jP1.add(jtfIP);
		jP1.add(jL2);
		jP1.add(jtfUser);
		jP1.add(jL3);
		jP1.add(jpdPass);

		myLayout.setConstraints(jL1, myGbc);
		myLayout.setConstraints(jtfIP, myGbc);
		myLayout.setConstraints(jL2, myGbc);
		myLayout.setConstraints(jtfUser, myGbc);
		myLayout.setConstraints(jL3, myGbc);
		myLayout.setConstraints(jpdPass, myGbc);

		JPanel jP2 = new JPanel();
		jP2.setLayout(myLayout);
		// Button 1&2
		JButton b1 = new JButton("UNSET");
		b1.setToolTipText("Close the Firewall!");
		b1.setPreferredSize(new Dimension(80, 30));
		b1.addActionListener(this);
		JButton b2 = new JButton("SET");
		b2.setToolTipText("Open the Firewall!");
		b2.setPreferredSize(new Dimension(80, 30));
		b2.addActionListener(this);
		jP2.add(b1);
		jP2.add(b2);

		JPanel jP3 = new JPanel();
		jP3.setLayout(myLayout);
		// Label
		// JLabel jL4 = new JLabel(info);
		jL4.setFont(new Font("Consolas", Font.BOLD, 13));
//		ImageIcon jL4_icon = new ImageIcon("images/jL4_icon.png");
		ImageIcon jL4_icon = new ImageIcon(this.getClass().getClassLoader().getResource("images/jL4_icon.png"));
		jL4.setIcon(jL4_icon);
		jL4.setPreferredSize(new Dimension(500, 80));

		jP3.add(jL4);

		myJf.add(jP1);
		myJf.add(jP2);
		myJf.add(jP3);

		myGbc.gridy = 0;
		myLayout.setConstraints(jP1, myGbc);
		myGbc.gridy = 1;
		myLayout.setConstraints(jP2, myGbc);
		myGbc.gridy = 2;
		myLayout.setConstraints(jP3, myGbc);

		myJf.setVisible(true);
		myJf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		new Firewall().go();
	}

	public void actionPerformed(ActionEvent ae) {
		int iCon = 0;
		if (ae.getActionCommand().equals("UNSET")) {
			iCon = mySSH.startSSH(jtfIP.getText(), jtfUser.getText(), new String(jpdPass.getPassword()), 22);
			if (1 == iCon) {
				jL4.setText("Please Check the Host IP!");
			} else if (2 == iCon) {
				jL4.setText("Connection Time Out!");
			} else if (3 == iCon) {
				jL4.setText("Authentication Failed!");
			} else if (4 == iCon) {
				jL4.setText("Unknown Error!");
			} else if (0 == iCon) {
				jL4.setText("Connected!");
			}
			
			String sCmd = "";
			sCmd = mySSH.sendCommands("iptables -L OUTPUT");
			System.out.println(sCmd);
			
			if (sCmd.contains("owner UID match cgpexpert multiport dports 0:ftp,telnet:65535")) {
				mySSH.sendCommands("iptables -t filter -D OUTPUT -p tcp -m owner --uid-owner cgpexpert -m multiport --destination-ports 0:21,23:65535 -j DROP");
				mySSH.sendCommands("iptables -t filter -D OUTPUT -p udp -m owner --uid-owner cgpexpert -j DROP");
				jL4.setText("Firewall Closed!");
			}else if("" != sCmd) { 
				jL4.setText("Please Open the Firewall First!");
			}

			mySSH.sshDiscon();

		} else if (ae.getActionCommand().equals("SET")) {
			iCon = mySSH.startSSH(jtfIP.getText(), jtfUser.getText(), new String(jpdPass.getPassword()), 22);
			if (1 == iCon) {
				jL4.setText("Please Check the Host IP!");
			} else if (2 == iCon) {
				jL4.setText("Connection Time Out!");
			} else if (3 == iCon) {
				jL4.setText("Authentication Failed!");
			} else if (4 == iCon) {
				jL4.setText("Unknown Error!");
			} else if (0 == iCon) {
				jL4.setText("Connected!");
			}
			
			String sCmd = "";
			sCmd = mySSH.sendCommands("iptables -L OUTPUT");
			System.out.println(sCmd);
			
			if (sCmd.contains("owner UID match cgpexpert multiport dports 0:ftp,telnet:65535")) {
				
				jL4.setText("Please Close the Firewall First!");
			}else if("" != sCmd){
				mySSH.sendCommands("iptables -t filter -A OUTPUT -p tcp -m owner  --uid-owner cgpexpert -m multiport --destination-ports 0:21,23:65535 -j DROP");
				mySSH.sendCommands("iptables -t filter -A OUTPUT -p udp -m owner  --uid-owner cgpexpert  -j DROP");
				jL4.setText("Firewall Opened!");
			}
			
			
			mySSH.sshDiscon();

		}

	}
}
