package db.changelog

databaseChangeLog() {
    changeSet(author: 'Dancemind', id: '1-url_data-table') {
        comment '1-url_data-table'
        tagDatabase '1-url_data-table'

        createTable(tableName: 'url_data') {

            column(name: 'id', type: 'bigint', autoIncrement: true) {
                constraints(primaryKey: true)
            }

            column(name: 'long_url', type: 'text')

            column(name: 'short_url', type: 'text')

            column(name: 'comment', type: 'text')

            column(name: 'deleted', type: 'boolean') {
                defaultValueBoolean: 'false'
            }

            column(name: 'created_on', type: 'timestamp')
            column(name: 'created_by', type: 'bigint')

        }
    }
}