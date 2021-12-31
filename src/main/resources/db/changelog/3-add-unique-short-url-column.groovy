package db.changelog

databaseChangeLog() {
    changeSet(author: 'Dancemind', id: '3-unique-short-url-column') {
        comment '3-unique-short-url-column'
        tagDatabase '3-unique-short-url-column'

        addUniqueConstraint(columnNames: "short_url", tableName: "url_data")
    }
}