var IS = window.IS || {};

IS.currentUser = '';

IS.describe = function (event) {
    var creature = function (verb) {
        return verb + ' объект #' + event.id + (event.name ? ' «' + event.name + '»' : '');
    };
    var withCreatures = function (text) {
        return event.affected ? text + ' вместе со связанными персонажами (' + event.affected + ')' : text;
    };
    switch (event.type) {
        case 'CREATURE_CREATED': return creature('создал');
        case 'CREATURE_UPDATED': return creature('изменил');
        case 'CREATURE_DELETED': return creature('удалил');
        case 'RINGS_SWAPPED': return 'обменял кольца персонажей #' + event.id + ' и #' + event.secondId;
        case 'COORDINATES_DELETED': return withCreatures('удалил координаты #' + event.id);
        case 'CITY_DELETED': return withCreatures('удалил город #' + event.id);
        case 'RING_DELETED': return 'удалил кольцо #' + event.id;
        default: return 'изменил данные';
    }
};

IS.onChange = function (message) {
    var event = typeof message === 'string' ? JSON.parse(message) : message;

    if (event.actor && event.actor !== IS.currentUser && window.PF && PF('notifier')) {
        PF('notifier').renderMessage({
            summary: 'Пользователь ' + event.actor,
            detail: IS.describe(event),
            severity: 'info'
        });
    }
    if (typeof window.refreshPage === 'function') {
        window.refreshPage([{name: 'changedId', value: event.id == null ? '' : String(event.id)},
                            {name: 'changeType', value: event.type}]);
    }
};

IS.afterSave = function (args, dialog) {
    if (args && !args.validationFailed && args.saved) {
        PF(dialog).hide();
    }
};
