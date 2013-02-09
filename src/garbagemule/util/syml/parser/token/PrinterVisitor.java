package garbagemule.util.syml.parser.token;

public class PrinterVisitor implements TokenVisitor<Void> {
    @Override
    public Void visitIndent(IndentToken token) {
        System.out.print(token.value());
        return null;
    }

    @Override
    public Void visitLineBreak(LineBreakToken token) {
        System.out.print('\n');
        return null;
    }

    @Override
    public Void visitComment(CommentToken token) {
        System.out.print("#" + token.value());
        return null;
    }

    @Override
    public Void visitKey(KeyToken token) {
        System.out.print(token.value());
        return null;
    }

    @Override
    public Void visitValue(ValueToken token) {
        System.out.print(token.value());
        return null;
    }

    @Override
    public Void visitListItem(ListItemToken token) {
        System.out.print("- " + token.value());
        return null;
    }

    @Override
    public Void visitColon(ColonToken token) {
        System.out.print(": ");
        return null;
    }
}
