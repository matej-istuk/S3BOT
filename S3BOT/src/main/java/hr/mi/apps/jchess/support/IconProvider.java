package hr.mi.apps.jchess.support;

import hr.mi.chess.models.ChessPiece;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Singleton class providing piece icons for the <code>ChessGuiApp</code>.
 * @author Matej Istuk
 */
public class IconProvider {
    private final Image whitePawnIcon;
    private final Image whiteRookIcon;
    private final Image whiteKnightIcon;
    private final Image whiteBishopIcon;
    private final Image whiteQueenIcon;
    private final Image whiteKingIcon;
    private final Image blackPawnIcon;
    private final Image blackRookIcon;
    private final Image blackKnightIcon;
    private final Image blackBishopIcon;
    private final Image blackQueenIcon;
    private final Image blackKingIcon;


    private static final IconProvider instance = new IconProvider();

    /**
     * Returns the only instance of the icon provider.
     * @return instance of the icon provider
     */
    public static IconProvider getInstance(){
        return instance;
    }

    /**
     * The constructor.
     */
    private IconProvider(){
        try {
            whitePawnIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/WhitePawn.png")));
            whiteRookIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/WhiteRook.png")));
            whiteKnightIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/WhiteKnight.png")));
            whiteBishopIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/WhiteBishop.png")));
            whiteQueenIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/WhiteQueen.png")));
            whiteKingIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/WhiteKing.png")));
            blackPawnIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/BlackPawn.png")));
            blackRookIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/BlackRook.png")));
            blackKnightIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/BlackKnight.png")));
            blackBishopIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/BlackBishop.png")));
            blackQueenIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/BlackQueen.png")));
            blackKingIcon = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource("ChessPieceIcons/Standard/BlackKing.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the image for the requested piece.
     * @param piece requested piece
     * @return image of the requested piece
     */
    public Image getPieceIcon(ChessPiece piece) {

        return switch (piece) {
            case WHITE_PAWN -> whitePawnIcon;
            case WHITE_ROOK -> whiteRookIcon;
            case WHITE_KNIGHT -> whiteKnightIcon;
            case WHITE_BISHOP -> whiteBishopIcon;
            case WHITE_QUEEN -> whiteQueenIcon;
            case WHITE_KING -> whiteKingIcon;
            case BLACK_PAWN -> blackPawnIcon;
            case BLACK_ROOK -> blackRookIcon;
            case BLACK_KNIGHT -> blackKnightIcon;
            case BLACK_BISHOP -> blackBishopIcon;
            case BLACK_QUEEN -> blackQueenIcon;
            case BLACK_KING -> blackKingIcon;
        };
    }
}
