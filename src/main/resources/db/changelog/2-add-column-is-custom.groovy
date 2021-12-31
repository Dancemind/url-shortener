package db.changelog

databaseChangeLog() {
    changeSet(author: 'Dancemind', id: '2-is-custom-column') {
        comment '2-is-custom-column'
        tagDatabase '2-is-custom-column'

        addColumn(tableName: 'url_data') {
            column(name: 'is_custom', type: 'boolean', defaultValueBoolean: 'false')
        }
    }
}