S3BOT is a chess engine created by me, Matej Ištuk, in Java.

S3BOT is made up of three main parts: the move generator, the game tree search function, and the evaluation function.

The package locations of the aforementioned parts are the following:
 The move generator: hr.mi.chess.movegen.LegalMoveGenerator
 The game tree search function: hr.mi.chess.algorithm.GameStateSearch
 The evaluation function: hr.mi.chess.evaluation.SimplePlusEvaluationFunction

You can play chess against my AI in three ways: 
 1. Using a GUI app: hr.mi.apps.jchess.ChessGuiApp
 2. Using a terminal app: hr.mi.apps.tchess.ChessTerminalApp
 3. Using the UCI protocol (this is mostly for other applications or study purposes): hr.mi.apps.uci.UCI
 I recommend using the GUI app :)

I plan on mainly improving the evaluation function of the engine.

I'm still working on some of the finishing touches (and there may be some bugs lurking).


P.S.
 As a quick demonstration, below is a PGN (portable game notation) of a game S3BOT played against a decent chess player. 
 The game can be viewed online (for example on https://www.chess.com/analysis?tab=analysis)
 
1. e4 d5 2. exd5 Qxd5 3. Ne2 Nf6 4. d4 a6 5. Nbc3 Qd6 6. Bf4 Qd8 7. Qd3 e6 8.
Qg3 Bd6 9. Bxd6 cxd6 10. Qxg7 Rg8 11. Qh6 b5 12. Nf4 Bb7 13. O-O-O Nc6 14. d5
exd5 15. Ncxd5 Nxd5 16. Nxd5 Ne7 17. Nf6# 1-0

