# How do I:

- Allow submission of other arbitrary languages:
  - Call `kothComm.addLanguage(new OtherLoader(YourPipePlayer::new))`.  YourPipePlayer is a class you need to make that implements your Player class.  The constructor needs to take a PipeCommuicator, and you will use that communicator to send messages in each of the functions.  [Example](https://github.com/nathanmerrill/StockExchange/blob/master/src/main/java/com/ppcg/stockexchange/PipeBot.java)

- Download submissions from StackExchange:
  - When you run your program, pass in `-q 012345`, replacing the number with the question ID of the stack exchange post.  Submission posts should:
    - Have a header file with their name, then a comma, then the language they are written in
    - Put their submission in a multi-line code block.  The first line should be the file name, and Java submissions should not have a package declaration
    - Multiple multi-line code blocks are fine
    - Other languages should include a `command.txt` which contains commands to execute the submission

- Manually add a submission:
  - If they are Java submission in your classpath, call `kothComm.addSubmission("submissionName", YourSubmission::new)`
  - If they are any other language, put them in the folder `submissions/other`, add a command.txt, and add the OtherLoader (as listed above)
  
