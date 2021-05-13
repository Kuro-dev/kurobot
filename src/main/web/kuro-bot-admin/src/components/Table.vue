<template>

    <table>
        <slot />
    </table>
    
</template>

<script>
export default {
    name: "Table"
}
</script>

<style scoped>

table {
    border-radius: 6px;
    table-layout: fixed;
    border-collapse: collapse;
}

table th {
    text-align: left;
    padding: 14px;
    background-color: rgba(0, 0, 0, 0.05);
    color: rgba(0, 0, 0, 0.5);
}

table th:first-child {
    border-radius: var(--border-radius) 0 0 var(--border-radius) ;
}

table th:last-child {
    border-radius: 0 var(--border-radius) var(--border-radius) 0;
}

table tr {
    border-radius: 6px;
}

table td {
    padding: 14px 10px;
    font-size: 18px;
    word-break: break-word;
}

</style>