package db.changelog

databaseChangeLog() {
    include(file: 'db/changelog/create-table-urls.groovy')
}