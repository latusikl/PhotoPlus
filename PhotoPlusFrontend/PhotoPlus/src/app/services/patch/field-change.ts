export class FieldChange {

    private changedFields: Map<string, string> = new Map<string, string>();

    registerChange(key: string, value: string): void {
        if (this.changedFields.has(key)) {
            this.changedFields.delete(key);
        }
        this.changedFields.set(key, value);
    }

    get map(): Map<string, string> {
        return this.changedFields;
    }

}