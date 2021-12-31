package db.changelog

databaseChangeLog() {
    include(file: 'db/changelog/create-table-urls.groovy')
    include(file: 'db/changelog/2-add-column-is-custom.groovy')
    include(file: 'db/changelog/3-add-unique-short-url-column.groovy')
}