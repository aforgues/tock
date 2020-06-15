function onTogglePawn(element, pawnNumber, isPlayable, targetPawnPosition) {
    if (isPlayable == 'true') {
        _togglePawn(element, pawnNumber, isPlayable, targetPawnPosition, 'active');
    }
    else {
        // Check that a pawn has already been selected to play
        const activePawns = document.getElementsByClassName("active");
        if (!activePawns || activePawns.length === 0)
            return false;

        // Select targeted pawn in case of JACK card
        const cardSelected = document.getElementById("cardId").value;
        const cardValue = cardSelected && cardSelected.split('-')[1];
        if (cardValue && cardValue === "jack" && targetPawnPosition) {
            _togglePawn(element, pawnNumber, isPlayable, targetPawnPosition, 'targeted');
        }
        else
            return false;
    }
}

function _togglePawn(element, pawnNumber, isPlayable, targetPawnPosition, classValue) {
    let formInputId = 'pawnNumber';
    let formInputValue = pawnNumber;
    if (classValue === 'targeted') {
        formInputId = 'targetPosition';
        formInputValue = targetPawnPosition;
    }

    if (element.className.endsWith(classValue)) {
        element.className = element.className.substring(0, element.className.indexOf(classValue)-1);
        document.getElementById(formInputId).value = '';
    }
    else {

        // first unselect other pawn with same classValue
        const pawns = document.getElementsByClassName(classValue);
        if (pawns && pawns.length > 0) {
            for (let i = 0; i < pawns.length; i++) {
                onTogglePawn(pawns[i], pawnNumber, isPlayable, targetPawnPosition);
            }
        }

        element.className = element.className + " " + classValue;
        document.getElementById(formInputId).value = formInputValue;
    }
}

function restorePlayerChoices(currentPlayerPawnColor) {
    const cardId = document.getElementById('cardId').value;
    if (cardId) {
        onCardClick(document.getElementById('card-' + cardId), cardId);
    }

    const pawnNumberValue = document.getElementById('pawnNumber').value;
    if (pawnNumberValue) {
        const sourcePawnElement = document.getElementById(currentPlayerPawnColor + '-' + pawnNumberValue);
        onTogglePawn(sourcePawnElement, pawnNumberValue, 'true', '');
    }

    const targetPositionValue = document.getElementById('targetPosition').value;
    if (targetPositionValue) {
        const targetPawnElement = document.getElementById(targetPositionValue).firstElementChild;
        if (targetPawnElement) {
            onTogglePawn(targetPawnElement, '', 'false', targetPositionValue);
        }
    }
}

function onCardClick(element, cardId) {
    const classValue = 'selectedCard';
    const formInputId =  'cardId';
    if (element.className.endsWith(classValue)) {
        element.className = element.className.substring(0, element.className.indexOf(classValue)-1);
        document.getElementById(formInputId).value = '';
    }
    else {
        // first unselect other pawn with same classValue
        const cards = document.getElementsByClassName(classValue);
        if (cards && cards.length > 0) {
            for (let i = 0; i < cards.length; i++) {
                onCardClick(cards[i]);
            }
        }

        element.className = element.className + " " + classValue;
        document.getElementById(formInputId).value = cardId;

        // unselect targetPosition if needed
        _checkTargetPosition();
    }
}

function _checkTargetPosition() {
    const selectedCard = document.getElementById('cardId').value;
    const cardValue = selectedCard.split('-')[1];

    if (cardValue !== "jack") {
        // first unselect targeted pawn
        const targetedPawns = document.getElementsByClassName("targeted");
        if (targetedPawns && targetedPawns.length > 0) {
            for (let i = 0; i < targetedPawns.length; i++) {
                let element = targetedPawns[i];
                element.className = element.className.substring(0, element.className.indexOf("targeted") - 1);
            }
        }
        document.getElementById('targetPosition').value = '';
    }
}