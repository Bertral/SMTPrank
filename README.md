# SMTPrank
SMTPrank is a small java command line application that sends custom mails from faked email addresses to confuse a group of people. Each recipient will see a person from the same group as sender.

## How to use it
- Install or update Java RE : https://www.java.com/en/download/
- Download the executable here : https://github.com/Bertral/SMTPrank/raw/master/executable/SMTPrank.jar
- Create a file named "addressList.txt" in the same folder. It must contain all the target email addresses, one by line.
- Create a file named "fakeMails.txt" in the same filder. It must contain the contents of the mails you want to send. To specify a subject, begin the mail with "Subject: ", then add a blank line and write the message body. Mails must be separated by 5 equal symbols on it's own line : "=====". The file MUST use Windows carriage returns (\r\n).
- In case of doubt on those two files' format, one can downlad and edit the samples already at the root of this repository.
- Open a terminal in the same folder and enter the following line : "java -jar server port n" where "server" is the address of the SMTP server that will send the emails, "port" his SMTP port (most likely "25"), and "n" the number of senders/groups.

## Implementation
The code is fairly simple and is basically just a script. Everything is contained in one class, and separated into 3 methods :
- main parses the files and calls sendMails,
- sendMails splits the target emails into groups, choses a sender and a mail to send, and calls sendMail,
- sendMail complies with the SMTP protocol to properly send a mail.

## Set up a test environement
If you want to try it in a test environement, you can set up a mock SMTP server on your one computer.
- Download MockMock : https://github.com/tweakers-dev/MockMock
- Open a terminal in the same folder and enter the following line : "java -jar MockMock.jar -p 25000"
- Use SMTPrank to send your emails through SMTP server "localhost" on port "25000" (so "java -jar localhost 25000 3" for example).
- Open a web browser and go to "localhost:8282". All sent messages will be listed on the web interface.
